package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.security.JwtUtils;
import com.example.thesimpleeventapp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
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

    @GetMapping("/user/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws MalformedURLException {
        return userService.loadImage(id);
    }

    @GetMapping("user/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        UserProfileDto userProfileDto = userService.getUserProfileById(userId);
        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping
    public List<UserRequestDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("user/{id}/public-profile")
    public ResponseEntity<UserPublicProfileDto> getPublicProfile(@PathVariable Long id) {
        UserPublicProfileDto profileDto = userService.getPublicProfileById(id);
        return ResponseEntity.ok(profileDto);
    }

    @PostMapping("user/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestHeader("Authorization") String authHeader, @RequestParam("image") MultipartFile file) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        String imageUrl = userService.saveProfilePicture(userId, file);
        return ResponseEntity.ok("Profile picture updated successfully.");
    }

    @PostMapping("/admin/createUser")
    public User createUser(@RequestBody CreateUserDto userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody PasswordChangeRequestDto requestDTO) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        userService.changePassword(userId, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping("user/update-profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateUserProfileDto dto) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        UserProfileDto userProfileDto = userService.updateUserProfile(userId, dto);
        return ResponseEntity.ok(userProfileDto);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("user/removeImage")
    public ResponseEntity<String> deleteImage(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);
        userService.deleteImage(userId);
        return ResponseEntity.ok("Profile picture updated successfully.");
    }
}