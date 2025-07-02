package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByEventId(Long eventId);

}
