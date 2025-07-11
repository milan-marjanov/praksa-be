package com.example.thesimpleeventapp.controller;


import com.example.thesimpleeventapp.dto.event.CreateEventDto;
import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.dto.vote.VoteDto;
import com.example.thesimpleeventapp.security.JwtUtils;
import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.event.UserEventsResponseDto;
import com.example.thesimpleeventapp.service.event.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final JwtUtils jwtUtils;


    @Autowired
    public EventController(EventService eventService,JwtUtils jwtUtils) {
        this.eventService = eventService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/fetchAllEvents")
    public List<EventDto> fetchAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/fetchUserEvents/{userId}")
    public UserEventsResponseDto fetchUserEvents(@PathVariable Long userId) {
        List<EventDto> allEvents = eventService.getAllEvents();

        List<EventDto> createdEvents = allEvents.stream()
                .filter(eventDto -> eventDto.getCreator().getId().equals(userId))
                .toList();

        List<EventDto> participantEvents = allEvents.stream()
                .filter(eventDto -> eventDto.getParticipants().stream()
                        .anyMatch(userDto -> userDto.getId().equals(userId)))
                .toList();

        return new UserEventsResponseDto(createdEvents, participantEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDto> getEventDetails(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);
        EventDetailsDto eventDetails = eventService.getEventDetails(id, userId);
        return ResponseEntity.ok(eventDetails);
    }

    @PostMapping("/createEvent")
    public EventDto createEvent(@RequestBody CreateEventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PostMapping("/voting")
    public ResponseEntity<VoteDto> submitVote(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody CreateVote voteRequestDto) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);
        VoteDto vote = eventService.voteForEvent(voteRequestDto,userId);
        return ResponseEntity.ok(vote);
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