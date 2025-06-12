package com.example.thesimpleeventapp.service.event;


import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();
    EventDto createEvent(EventDto eventDto, Long creatorId);

import com.example.thesimpleeventapp.dto.event.CreateEventDto;
import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<EventDto> getAllEvents();
    Event getEventById(Long id);
    EventDto createEvent(CreateEventDto eventDto);
    EventDto updateEvent(UpdateEventDto eventDto, Long eventId);

}
