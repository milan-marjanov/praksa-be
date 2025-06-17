package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.CreateEventDto;
import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();

    Event getEventById(Long id);

    EventDto createEvent(CreateEventDto eventDto);

    EventDto updateEvent(UpdateEventDto eventDto, Long eventId);

    void deleteEvent(Long eventId);

    boolean voteForEvent(CreateVote dto,Long userId);
  
    List<EventBasicDto> getAllBasicEvents();

    EventDetailsDto getEventDetails(long id);

}
