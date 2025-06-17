package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.CreateEventDto;
import com.example.thesimpleeventapp.dto.event.EventBasicDto;
import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();

    Event getEventById(Long id);

    EventDto createEvent(CreateEventDto eventDto);

    EventDto updateEvent(UpdateEventDto eventDto, Long eventId);

    void deleteEvent(Long eventId);

    List<EventBasicDto> getAllBasicEvents();
}
