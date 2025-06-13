package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.user.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface UserService {
    User saveUserWithDefaults(CreateUserDto createUserDTO);

    List<UserRequestDTO> getAllUsers();

    User getUserById(Long id);

    UserRequestDTO getUserDtoById(Long id);

    void deleteUser(Long id);

    void changePassword(
            Long userId,
            PasswordChangeRequestDTO passwordDTO);

    List<User> getUserByIds(List<Long> id);
}
