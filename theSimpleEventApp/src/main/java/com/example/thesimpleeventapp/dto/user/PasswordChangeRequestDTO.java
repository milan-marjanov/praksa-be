package com.example.thesimpleeventapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PasswordChangeRequestDTO {

    @NotBlank(message = "Old password must not be blank")
    private String oldPassword;

    @NotBlank(message = "Old password must not be blank")
    private String oldPasswordConfirm;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;
}
