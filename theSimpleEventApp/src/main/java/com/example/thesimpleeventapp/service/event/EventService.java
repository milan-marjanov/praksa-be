package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.dto.vote.VoteDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;

public interface EventService {

    List<EventDto> getAllEvents();

    EventDetailsDto getEventDetails(long eventId, long userId);

    EventDto createEvent(CreateEventDto eventDto);

    EventDto updateEvent(UpdateEventDto eventDto, Long eventId);

    VoteDto voteForEvent(CreateVote dto, Long userId);

    void deleteEvent(Long eventId);








}
