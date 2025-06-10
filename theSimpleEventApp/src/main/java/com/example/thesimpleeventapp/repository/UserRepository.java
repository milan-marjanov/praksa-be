package com.example.thesimpleeventapp.repository;

import com.example.thesimpleeventapp.dto.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
