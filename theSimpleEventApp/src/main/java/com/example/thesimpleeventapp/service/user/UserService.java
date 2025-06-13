package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface UserService {
    User saveUserWithDefaults(CreateUserDto createUserDTO);

    List<UserRequestDto> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);

    void changePassword(Long userId, PasswordChangeRequestDto passwordDTO);

    UserProfileDto getUserProfileById(Long id);

    UserProfileDto updateUserProfile(Long userId, UpdateUserProfileDto dto);

    UserPublicProfileDto getPublicProfileById(Long id);

    void updateProfilePicture(Long userId, ProfilePictureUpdateDto dto);

}
