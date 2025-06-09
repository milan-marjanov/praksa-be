package com.example.thesimpleeventapp.dto.user;

import com.example.thesimpleeventapp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String profilePictureUrl;

    private Role role;
}