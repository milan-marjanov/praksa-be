package com.example.thesimpleeventapp.service.notification;

public interface NotificationService {

    void markAsRead(Long id,Long userId);

    void deleteNotification(Long notificationId, Long userId);
}
