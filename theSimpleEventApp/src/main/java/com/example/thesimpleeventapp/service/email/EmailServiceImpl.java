package com.example.thesimpleeventapp.service.email;

import com.example.thesimpleeventapp.dto.notification.NotificationDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private String appBaseUrl;


    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        Dotenv dotenv = Dotenv.load();
        this.appBaseUrl = dotenv.get("APP_BASE_URL");

    }

    @Override
    public void sendUserCreationEmail(User user, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Account creation");
        message.setText("Hello " + user.getFirstName() + ",\n\n" +
                "Your account has been created.\n\n" +
                "Here are your details:\n" +
                "Email: " + user.getEmail() + "\n" +
                "Temporary password: " + password + "\n\n" +
                "Please login and change your password.\n\n" +
                "Regards,\nYour App Team");

        mailSender.send(message);
    }

    @Override
    public void sendUserNotificationEmail(User user, NotificationDto notificationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Event update: " + notificationDto.getTitle());
        String eventLink = appBaseUrl + "/api/events/" + user.getId();
        String emailContent = "Hello " + user.getFirstName() + ",\n\n" +
                "You have a new notification regarding your event.\n\n" +
                "Link to event: " + eventLink;
        message.setText(emailContent);
        mailSender.send(message);
    }

    @Override
    public void sendVotingReminderEmail(User user, Event event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Voting Deadline Approaching!");
        String eventLink = appBaseUrl + "/api/events/" + user.getId();
        String emailContent = "Hello " + user.getFirstName() + ",\n\n" +
                "Reminder: The voting deadline for the event \"" + event.getTitle() + "\" is approaching!\n\n" +
                "Please cast your vote before: " + event.getVotingDeadline() + "\n" +
                "Event link: " + eventLink + "\n\n" +
                "Thank you,\nYour Event App Team";
        message.setText(emailContent);
        mailSender.send(message);
    }
}
