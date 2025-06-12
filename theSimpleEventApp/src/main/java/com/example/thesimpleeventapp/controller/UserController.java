package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.user.ProfilePictureUpdateDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/createUser")
    public User createUser(@RequestBody CreateUserDto userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }

    @PostMapping("/user/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequestDTO requestDTO) {
        userService.changePassword(id, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping
    public List<UserRequestDTO> getAllUsers() {
        return userService.getAllUsers();
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
