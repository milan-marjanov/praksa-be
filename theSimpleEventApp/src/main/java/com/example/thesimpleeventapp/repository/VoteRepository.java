package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserIdAndEventId(Long userId,Long eventId);
}
