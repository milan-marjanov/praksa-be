package com.example.thesimpleeventapp.service.notification;

import com.example.thesimpleeventapp.dto.mapper.NotificationMapper;
import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.Notification;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.NotificationRepository;
import com.example.thesimpleeventapp.service.event.EventService;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }


    @Override
    public void createNotification(Event event, User user) {
        Notification newNotification = Notification.builder()
                .title("Event creation")
                .text("You have been invited to an event " + event.getTitle())
                .event(event)
                .user(user)
                .isRead(false).createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(newNotification);
    }

    @Override
    public List<NotificationDto> fetchNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        return notifications.stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }
}
