package com.example.thesimpleeventapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequestDTO {
    private String oldPassword;
    private String oldPasswordConfirm;
    private String newPassword;
}
