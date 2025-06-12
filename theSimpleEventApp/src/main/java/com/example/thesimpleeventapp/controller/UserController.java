package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.UserProfileDto;
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
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/admin/createUser")
    public User createUser(@RequestBody CreateUserDto userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }

    @PostMapping("/user/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id,@Valid @RequestBody PasswordChangeRequestDTO requestDTO){
        userService.changePassword(id, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }


    @GetMapping("user/{id}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        UserProfileDto userProfileDto = userService.getUserProfileById(id);
        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping
    public List<UserRequestDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("user/{id}/profile")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id,@Valid @RequestBody UserProfileDto dto) {
        userService.updateUserProfile(id, dto);
        return ResponseEntity.ok("Profile updated successfully.");
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}
