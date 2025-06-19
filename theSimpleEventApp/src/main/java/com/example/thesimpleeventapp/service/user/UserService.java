package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDto;
import com.example.thesimpleeventapp.model.User;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface UserService {

    User saveUserWithDefaults(CreateUserDto createUserDTO);

    List<UserRequestDto> getAllUsers();

    User getUserById(Long id);

    UserRequestDto getUserDtoById(Long id);

    void deleteUser(Long id);

    void changePassword(Long userId, PasswordChangeRequestDto passwordDTO);

    UserProfileDto getUserProfileById(Long id);

    UserProfileDto updateUserProfile(Long userId, UpdateUserProfileDto dto);

    UserPublicProfileDto getPublicProfileById(Long id);

    List<User> getUserByIds(List<Long> id);

    String saveProfilePicture(Long userId, MultipartFile file);

    ResponseEntity<Resource> loadImage(Long userId) throws MalformedURLException;

    void deleteImage(Long id);

}
