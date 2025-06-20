package com.example.thesimpleeventapp.service.notification;


import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.Notification;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.NotificationRepository;
import com.example.thesimpleeventapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
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
