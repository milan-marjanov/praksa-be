package com.example.thesimpleeventapp.service.email;

import com.example.thesimpleeventapp.model.User;

public interface EmailService {
    void sendUserCreationEmail(User user,String password);
}
