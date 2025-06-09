package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.model.DTO.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface UserService {
    User saveUserWithDefaults(UserRequestDTO userRequestDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    void changePassword(
            Long userId,
            String oldPassword,
            String oldPasswordConfirm,
            String newPassword);
}
