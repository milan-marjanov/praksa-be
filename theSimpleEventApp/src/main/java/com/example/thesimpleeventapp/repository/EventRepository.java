package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
