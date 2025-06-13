package com.example.thesimpleeventapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDto {

    @NotBlank(message = "Old password must not be blank")
    private String oldPassword;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    @NotBlank(message = "New password confirmation must not be blank")
    private String newPasswordConfirm;
}
