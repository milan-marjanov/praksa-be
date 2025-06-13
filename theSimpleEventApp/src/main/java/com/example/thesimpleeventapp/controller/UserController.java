package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.security.JwtUtils;
import com.example.thesimpleeventapp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {

        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/admin/createUser")
    public User createUser(@RequestBody CreateUserDto userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }


    @PostMapping("/user/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PasswordChangeRequestDTO requestDTO) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        userService.changePassword(userId, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("user/{id}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        UserProfileDto userProfileDto = userService.getUserProfileById(id);
        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping
    public List<UserRequestDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("user/{id}/profile")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id, @Valid @RequestBody UserProfileDto dto) {
        userService.updateUserProfile(id, dto);
        return ResponseEntity.ok("Profile updated successfully.");
    }


    @GetMapping("user/{id}/public-profile")
    public ResponseEntity<UserPublicProfileDto> getPublicProfile(@PathVariable Long id) {
        UserPublicProfileDto profileDto = userService.getPublicProfileById(id);
        return ResponseEntity.ok(profileDto);
    }


    @PutMapping("/user/{id}/profile-picture")
    public ResponseEntity<String> updateProfilePicture(@PathVariable Long id, @Valid @RequestBody ProfilePictureUpdateDto dto) {
        userService.updateProfilePicture(id, dto);
        return ResponseEntity.ok("Profile picture updated successfully");
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
