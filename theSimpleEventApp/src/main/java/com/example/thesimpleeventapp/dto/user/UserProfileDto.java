package com.example.thesimpleeventapp.dto.user;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class UserProfileDto {

    private String firstName;

    private String lastName;

    private String email;

}
