package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.Notification;

public class NotificationMapper {
    public static NotificationDto toDto(Notification notification){
        return NotificationDto.builder()
                .id(notification.getId())
                .text(notification.getText())
                .title(notification.getTitle())
                .eventId(notification.getEvent().getId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
