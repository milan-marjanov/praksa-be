package com.example.thesimpleeventapp.dto.user;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserProfileDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Profile picture URL is required")
    private String profilePictureUrl;
}
