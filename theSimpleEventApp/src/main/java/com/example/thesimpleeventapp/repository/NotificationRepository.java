package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long userId);

    void deleteByEventId(@Param("eventId")Long eventId);

    boolean existsByTitleAndEventIdAndUserId(String title, Long eventId, Long userId);
}
