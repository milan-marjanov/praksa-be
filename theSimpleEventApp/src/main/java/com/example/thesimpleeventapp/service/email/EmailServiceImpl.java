package com.example.thesimpleeventapp.service.email;

import com.example.thesimpleeventapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Override
    public void sendUserCreationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Account creation");
        message.setText("Hello " + user.getFirstName() + ",\n\n" +
                "Your account has been created.\n\n" +
                "Here are your details:\n" +
                "Email: " + user.getEmail() + "\n" +
                "Temporary password: " + user.getPassword() + "\n\n" +
                "Please login and change your password.\n\n" +
                "Regards,\nYour App Team");

        mailSender.send(message);
    }
}
