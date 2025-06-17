package com.example.thesimpleeventapp.controller;


import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<EventDto> fetchAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/basic")
    public List<EventBasicDto> getAllBasicEvents() {
        return eventService.getAllBasicEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDto> getEventDetails(@PathVariable Long id) {
        EventDetailsDto eventDetails = eventService.getEventDetails(id);
        return ResponseEntity.ok(eventDetails);
    }

    @PostMapping("/createEvent")
    public EventDto createEvent(@RequestBody CreateEventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PatchMapping("/updateEvent/{eventId}")
    public EventDto updateEvent(@RequestBody UpdateEventDto updateEventDto, @PathVariable Long eventId) {
        return eventService.updateEvent(updateEventDto, eventId);
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }


}
