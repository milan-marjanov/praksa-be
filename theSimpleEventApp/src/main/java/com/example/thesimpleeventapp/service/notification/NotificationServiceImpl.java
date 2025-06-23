package com.example.thesimpleeventapp.service.notification;

import com.example.thesimpleeventapp.dto.mapper.NotificationMapper;
import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.Notification;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.NotificationRepository;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
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

    @Override
    public List<NotificationDto> fetchNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return notifications.stream()
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
