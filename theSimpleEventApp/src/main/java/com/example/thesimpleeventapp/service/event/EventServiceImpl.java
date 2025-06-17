package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.mapper.EventMapper;
import com.example.thesimpleeventapp.dto.mapper.TimeOptionMapper;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidTimeOptionException;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.TimeOption;
import com.example.thesimpleeventapp.model.TimeOptionType;
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

    private void validateTimeOptions(TimeOptionType optionType, List<TimeOptionDto> timeOptionDtos) {
        switch (optionType) {
            case FIXED:
                if (timeOptionDtos.size() != 1) {
                    throw new InvalidTimeOptionException("For FIXED option type, exactly 1 time option must be provided.");
                }
                break;

            case VOTING:
                if (timeOptionDtos.isEmpty() || timeOptionDtos.size() < 2 || timeOptionDtos.size() > 6) {
                    throw new InvalidTimeOptionException("For VOTING option type, between 2 and 6 time options must be provided.");
                }
                break;

            case CAPACITY_BASED:
                boolean invalidCapacity = timeOptionDtos.stream()
                        .anyMatch(dto -> dto.getMaxCapacity() == null || dto.getMaxCapacity() <= 0);
                if (invalidCapacity) {
                    throw new InvalidTimeOptionException("For CAPACITY BASED option type, each time option must have a maxCapacity greater than 0.");
                }
                break;

            default:
                throw new InvalidTimeOptionException("Unsupported time option type.");
        }
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
        // Validate Event basic data
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
                .timeOptionType(eventDto.getTimeOptionType())
                .build();

        List<TimeOptionDto> timeOptionDtos = eventDto.getTimeOptions();

        if (timeOptionDtos != null && !timeOptionDtos.isEmpty()) {
            validateTimeOptions(newEvent.getTimeOptionType(), timeOptionDtos);

            List<TimeOption> timeOptionEntities = timeOptionDtos.stream()
                    .map(TimeOptionMapper::toEntity)
                    .toList();

            timeOptionEntities.forEach(option -> option.setEvent(newEvent));
            newEvent.getTimeOptions().addAll(timeOptionEntities);
        }

        List<TimeOption> timeOptionEntities = timeOptionDtos.stream()
                .map(TimeOptionMapper::toEntity)
                .toList();

        timeOptionEntities.forEach(option -> option.setEvent(newEvent));

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
}