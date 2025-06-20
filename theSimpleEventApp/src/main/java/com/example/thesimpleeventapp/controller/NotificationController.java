package com.example.thesimpleeventapp.controller;


import com.example.thesimpleeventapp.security.JwtUtils;
import com.example.thesimpleeventapp.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;

    @Autowired
    public NotificationController(NotificationService notificationService, JwtUtils jwtUtils) {
        this.notificationService = notificationService;
        this.jwtUtils = jwtUtils;
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
