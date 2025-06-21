package com.example.thesimpleeventapp.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private Long eventId;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private boolean isRead;
}
