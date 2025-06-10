package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDTO;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/createUser")
    public User createUser(@RequestBody CreateUserDto userDTO) {
        return userService.saveUserWithDefaults(userDTO);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequestDTO requestDTO){
        userService.changePassword(id, requestDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public List<UserRequestDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }


    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/kkk")
    public String getSomething(){
        return  "Admin true";
    }

}
