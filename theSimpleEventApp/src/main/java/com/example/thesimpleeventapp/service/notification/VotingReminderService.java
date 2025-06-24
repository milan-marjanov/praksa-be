package com.example.thesimpleeventapp.service.notification;

import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.VoteRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import com.example.thesimpleeventapp.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class VotingReminderService {

    private final VoteRepository voteRepository;
    private final EventService eventService;
    private final EmailService emailService;


    @Autowired
    public VotingReminderService(VoteRepository voteRepository, EventService eventService, EmailService emailService) {
        this.voteRepository = voteRepository;
        this.eventService = eventService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRateString = "3h")
    public void checkVotingDeadlinesAndNotify(){
        List<EventDto> allEvents = eventService.getAllEvents();
        LocalDateTime currentTime = LocalDateTime.now();
        for (EventDto eventDto : allEvents){
            if (eventDto.getVotingDeadline() != null){
                Duration timeUntilDeadline = Duration.between(currentTime, eventDto.getVotingDeadline());
                if (!timeUntilDeadline.isNegative() && timeUntilDeadline.toHours() <= 24){
                    notifyUserWhoDidNotVote(eventDto);
                }
            }
        }
    }

    private void notifyUserWhoDidNotVote(EventDto eventDto){
        List<User> userToNotify = voteRepository.findUsersWhoDidNotVote(eventDto.getId());
        for (User user : userToNotify){
            emailService.sendVotingReminderEmail(user, eventDto);
        }
    }
}
