package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId")
    List<Event> findAllByParticipantId(@Param("userId") Long userId);

    List<Event> findByCreatorId(Long creatorId);
}