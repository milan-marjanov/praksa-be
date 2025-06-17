package com.example.thesimpleeventapp.dto.user;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ProfilePictureUpdateDto {

    @NotNull
    private String profilePictureUrl;

}
