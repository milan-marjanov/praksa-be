package com.example.thesimpleeventapp.model.DTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordChangeRequestDTO {
    private String oldPassword;
    private String oldPasswordConfirm;
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getOldPasswordConfirm() {
        return oldPasswordConfirm;
    }

    public void setOldPasswordConfirm(String oldPasswordConfirm) {
        this.oldPasswordConfirm = oldPasswordConfirm;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
