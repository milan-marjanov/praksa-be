package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents();
    EventDto createEvent(EventDto eventDto, Long creatorId);
}
