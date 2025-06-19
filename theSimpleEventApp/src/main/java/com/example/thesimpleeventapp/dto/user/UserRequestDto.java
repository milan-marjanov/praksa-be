package com.example.thesimpleeventapp.dto.user;

import com.example.thesimpleeventapp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String profilePicture;

    private Role role;
}
