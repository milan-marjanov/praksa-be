package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
