package com.example.thesimpleeventapp.service.notification;

import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.ParticipantDto;
import com.example.thesimpleeventapp.dto.mapper.NotificationMapper;
import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.Notification;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.NotificationRepository;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import com.example.thesimpleeventapp.service.event.EventService;
import com.example.thesimpleeventapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository,
                                   EmailService emailService,
                                   @Lazy EventService eventService,
                                   UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public void createNotification(String title, String text, Event event, User user) {
        Notification newNotification = Notification.builder()
                .title(title)
                .text(text)
                .event(event)
                .user(user)
                .isRead(false).createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(newNotification);

        emailService.sendUserNotificationEmail(user, NotificationMapper.toDto(newNotification));
    }

    @Scheduled(fixedRate = 50000) // every hour
    @Transactional
    public void checkVotingDeadlinesAndNotify() {
        List<EventDto> allEvents = eventService.getAllEvents();
        LocalDateTime currentTime = LocalDateTime.now();

        for (EventDto eventDto : allEvents) {
            LocalDateTime deadline = eventDto.getVotingDeadline();

            if (deadline != null) {
                Duration timeUntilDeadline = Duration.between(currentTime, deadline);
                long hoursUntilDeadline = timeUntilDeadline.toHours();

                if (hoursUntilDeadline <= 24) {
                    for (ParticipantDto participant : eventDto.getParticipants()) {
                        boolean alreadyNotified = notificationRepository.existsByTitleAndEventIdAndUserId(
                                "Deadline notification",
                                eventDto.getId(),
                                participant.getId()
                        );

                        if (!alreadyNotified) {
                            Event event = eventService.getEventById(eventDto.getId());
                            User user = userService.getUserById(participant.getId());
                            createNotification(
                                    "Deadline notification",
                                    "Only 24 hours left to vote for event: " + eventDto.getTitle(),
                                    event,
                                    user
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<NotificationDto> fetchNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return notifications.stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {

        Notification notification = getNotificationIfOwner(notificationId, userId);

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = getNotificationIfOwner(notificationId, userId);
        notificationRepository.delete(notification);
    }

    private Notification getNotificationIfOwner(Long notificationId, Long userId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        User userNotification = notification.getUser();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!userNotification.equals(user)) {
            throw new SecurityException("User is not authorized to perform this action on the notification.");
        }
        return notification;
    }
}
