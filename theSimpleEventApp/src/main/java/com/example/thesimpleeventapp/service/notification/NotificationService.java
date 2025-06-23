package com.example.thesimpleeventapp.service.notification;


import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface NotificationService {

    void createNotification(Event event, User user);

    List<NotificationDto> fetchNotifications(Long userId);

    void markAsRead(Long id,Long userId);

    void deleteNotification(Long notificationId, Long userId);


}