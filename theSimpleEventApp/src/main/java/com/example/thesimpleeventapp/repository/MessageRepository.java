package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Message;
import com.example.thesimpleeventapp.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    void deleteByUserId(Long userId);

    void deleteAllByUser(User user);
}
