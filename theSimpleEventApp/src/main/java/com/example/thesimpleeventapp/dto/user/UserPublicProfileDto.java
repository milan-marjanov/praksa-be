package com.example.thesimpleeventapp.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPublicProfileDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String profilePictureUrl;
}