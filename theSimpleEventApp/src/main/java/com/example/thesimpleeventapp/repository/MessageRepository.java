package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
