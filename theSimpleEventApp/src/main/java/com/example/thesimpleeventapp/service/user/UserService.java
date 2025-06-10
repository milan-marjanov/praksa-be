package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.CreateUserDTO;
import com.example.thesimpleeventapp.dto.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface UserService {
    User saveUserWithDefaults(CreateUserDTO createUserDTO);
    List<UserRequestDTO> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    void changePassword(
            Long userId,
            PasswordChangeRequestDTO passwordDTO);
}
