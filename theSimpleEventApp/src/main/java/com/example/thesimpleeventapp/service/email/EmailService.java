package com.example.thesimpleeventapp.service.email;

import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;

public interface EmailService {
    void sendUserCreationEmail(User user, String password);
    void sendUserNotificationEmail(User user, NotificationDto notificationDto);

    void sendVotingReminderEmail(User user, Event event);
}
