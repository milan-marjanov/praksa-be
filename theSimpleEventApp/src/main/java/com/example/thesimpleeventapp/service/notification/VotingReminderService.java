package com.example.thesimpleeventapp.service.notification;

import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.VoteRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class VotingReminderService {

    private final TaskScheduler taskScheduler;
    private final VoteRepository voteRepository;
    private final EmailService emailService;


    @Autowired
    public VotingReminderService(VoteRepository voteRepository, EmailService emailService) {
        this.voteRepository = voteRepository;
        this.emailService = emailService;
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

    public void scheduleVotingReminder(Event event) {
        if(event.getVotingDeadline() != null) {
            LocalDateTime reminderTime = event.getVotingDeadline().minusHours(24);
            Duration delay = Duration.between(LocalDateTime.now(), reminderTime);
            if (!delay.isNegative()) {
                taskScheduler.schedule(() -> sendVotingReminder(event),
                        Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant()));
            }
        }
    }

    private void sendVotingReminder(Event event) {
        List<User> usersToNotify = voteRepository.findUsersWhoDidNotVote(event.getId());
        for (User user : usersToNotify) {
            emailService.sendVotingReminderEmail(user, event);
        }
    }
}
