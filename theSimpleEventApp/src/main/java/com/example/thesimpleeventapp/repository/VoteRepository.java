package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT u FROM Event e JOIN e.participants u " +
            "WHERE e.id = :eventId AND u.id NOT IN " +
            "(SELECT v.user.id FROM Vote v WHERE v.event.id = :eventId)")
    List<User> findUsersWhoDidNotVote(@Param("eventId") Long eventId);
}
