package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.CreateEventDto;
import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.dto.mapper.EventMapper;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.model.*;
import com.example.thesimpleeventapp.repository.*;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TimeOptionRepository timeOptionRepository;
    private final RestaurantOptionRepository restaurantOptionRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserService userService,UserRepository userRepository,TimeOptionRepository timeOptionRepository,RestaurantOptionRepository restaurantOptionRepository,VoteRepository voteRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.timeOptionRepository = timeOptionRepository;
        this.restaurantOptionRepository = restaurantOptionRepository;
        this.voteRepository = voteRepository;
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
    public boolean voteForEvent(CreateVote dto,Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(dto.getEventId());

        if (userOpt.isEmpty() || eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event or user not found.");
        }
        Optional<Vote> existingVoteOpt = voteRepository.findByUserIdAndEventId(userId, dto.getEventId());

        if (existingVoteOpt.isPresent()) {
            Vote vote = existingVoteOpt.get();

            if (vote.getTimeOption().getId() != dto.getTimeOptionId()) {
                Optional<TimeOption> newTimeOpt = timeOptionRepository.findById(dto.getTimeOptionId());
                if (newTimeOpt.isEmpty()) {
                    throw new EventNotFoundException("Time option not found.");
                }
                vote.setTimeOption(newTimeOpt.get());
            }

            if (dto.getRestaurantOptionId() != null) {
                Optional<RestaurantOption> restOpt = restaurantOptionRepository.findById(dto.getRestaurantOptionId());
                if (restOpt.isEmpty()) {
                    throw new IllegalArgumentException("No restaurant found with the given ID.");
                }
                vote.setRestaurantOption(restOpt.get());
            }

            voteRepository.save(vote);
            return true;
        }


        Optional<TimeOption> timeOpt = timeOptionRepository.findById(dto.getTimeOptionId());
        if (timeOpt.isEmpty()) {
            throw new EventNotFoundException("Time option not found.");
        }

        Vote newVote = new Vote();
        newVote.setUser(userOpt.get());
        newVote.setEvent(eventOpt.get());
        newVote.setTimeOption(timeOpt.get());

        if (dto.getRestaurantOptionId() != null) {
            Optional<RestaurantOption> restOpt = restaurantOptionRepository.findById(dto.getRestaurantOptionId());
            if (restOpt.isEmpty()) {
                throw new IllegalArgumentException("No restaurant found with the given ID.");
            }
            newVote.setRestaurantOption(restOpt.get());
        }

        voteRepository.save(newVote);
        return true;
    }
}