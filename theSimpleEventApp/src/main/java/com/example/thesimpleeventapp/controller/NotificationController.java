package com.example.thesimpleeventapp.controller;


import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.security.JwtUtils;
import com.example.thesimpleeventapp.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public NotificationController(NotificationService notificationService,UserRepository userRepository,  JwtUtils jwtUtils) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        List<NotificationDto> unreadNotifications = notificationService.fetchNotifications(userId);
        return ResponseEntity.ok(unreadNotifications);

    }

    @PutMapping("/mark-as-read/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        notificationService.deleteNotification(id, userId);
        return ResponseEntity.ok().build();
    }
}
