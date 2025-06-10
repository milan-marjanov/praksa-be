package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.CreateUserDTO;
import com.example.thesimpleeventapp.dto.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody CreateUserDTO userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequestDTO requestDTO){
        userService.changePassword(id, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping
    public List<UserRequestDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}
