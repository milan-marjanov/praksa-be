package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/fetchAllEvents")
    public List<EventDto> fetchAllEvents(){
        return eventService.getAllEvents();
    }

    @PostMapping("/createEvent/{creatorId}")
    public EventDto createEvent(@RequestBody EventDto eventDto, @PathVariable Long creatorId){
        return eventService.createEvent(eventDto, creatorId);
    }
}
