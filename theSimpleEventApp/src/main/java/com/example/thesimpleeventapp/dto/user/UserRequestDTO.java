package com.example.thesimpleeventapp.dto.user;

import com.example.thesimpleeventapp.model.Role;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private Role role;
}
