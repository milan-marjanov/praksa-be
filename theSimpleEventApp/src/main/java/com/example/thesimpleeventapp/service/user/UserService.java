package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.model.User;

import java.util.List;

public interface UserService {
    User saveUserWithDefaults(CreateUserDto createUserDTO);

    List<UserRequestDTO> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);

    void changePassword(Long userId, PasswordChangeRequestDTO passwordDTO);

    UserProfileDto getUserProfileById(Long id);

    boolean updateUserProfile(Long userId, UserProfileDto dto);

    UserPublicProfileDto getPublicProfileById(Long id);

    void updateProfilePicture(Long userId, ProfilePictureUpdateDto dto);

}
