package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.mapper.EventMapper;
import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.RestaurantOption;
import com.example.thesimpleeventapp.model.TimeOption;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.EventRepository;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    @Override
    public List<EventDto> getAllEvents() {
        List<Event> events = Optional.of(eventRepository.findAll())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EventNotFoundException("No events found"));

        return events.stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Event getEventById(Long id) {
        return null;
    }

    @Override
    public EventDto createEvent(CreateEventDto eventDto) {
        if (eventDto.getTitle() == null || eventDto.getTitle().isBlank()) {
            throw new InvalidEventDataException("Event title must not be empty");
        }

        User creator = userService.getUserById(eventDto.getCreatorId());
        List<User> initialParticipants = userService.getUserByIds(eventDto.getParticipantIds());

        if (!initialParticipants.contains(creator)) {
            initialParticipants.add(0, creator);
        }

        Event newEvent = Event.builder()
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .creator(creator)
                .participants(initialParticipants)
                .timeOptions(new ArrayList<>())
                .restaurantOptions(new ArrayList<>())
                .chat(null)
                .votes(new ArrayList<>())
                .build();

        Event savedEvent = eventRepository.save(newEvent);
        return EventMapper.toDto(savedEvent);
    }

    @Override
    public EventDto updateEvent(UpdateEventDto eventDto, Long eventId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        List<User> users = userService.getUserByIds(eventDto.getParticipantIds());

        existingEvent.setTitle(eventDto.getTitle());
        existingEvent.setDescription(eventDto.getDescription());
        existingEvent.setParticipants(users);

        User creator = existingEvent.getCreator();
        if (!users.contains(creator)) {
            users.add(0, creator);
        }

        Event updatedEvent = eventRepository.save(existingEvent);

        return EventMapper.toDto(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }


    @Override
    public List<EventBasicDto> getAllBasicEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::convertToBasicDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDetailsDto getEventDetails(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        return EventDetailsDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .participants(event.getParticipants().stream().map(this::convertUserToDto).collect(Collectors.toList()))
                .timeOptions(event.getTimeOptions().stream().map(this::convertTimeOptionToDto).collect(Collectors.toList()))
                .restaurantOptions(event.getRestaurantOptions().stream().map(this::convertRestaurantToDto).collect(Collectors.toList()))
                .build();
    }

    private UserProfileDto convertUserToDto(User user) {
        return UserProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private TimeOptionDto convertTimeOptionToDto(TimeOption timeOption) {
        return TimeOptionDto.builder()
                .id(timeOption.getId())
                .startTime(timeOption.getStartTime())
                .endTime(timeOption.getEndTime())
                .deadline(timeOption.getDeadline())
                .build();
    }

    private RestaurantOptionDto convertRestaurantToDto(RestaurantOption restaurantOption) {
        return RestaurantOptionDto.builder()
                .id(restaurantOption.getId())
                .name(restaurantOption.getName())
                .menuImageUrl(restaurantOption.getMenuImageUrl())
                .restaurantUrl(restaurantOption.getRestaurantUrl())
                .build();
    }

    private EventBasicDto convertToBasicDto(Event event) {
        return EventBasicDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .build();
    }



}