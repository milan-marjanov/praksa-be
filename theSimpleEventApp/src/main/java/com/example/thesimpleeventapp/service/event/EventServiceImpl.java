package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.mapper.EventMapper;
import com.example.thesimpleeventapp.dto.mapper.RestaurantOptionMapper;
import com.example.thesimpleeventapp.dto.mapper.TimeOptionMapper;
import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import com.example.thesimpleeventapp.dto.mapper.RestaurantOptionMapper;
import com.example.thesimpleeventapp.dto.mapper.TimeOptionMapper;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidTimeOptionException;
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
                .timeOptionType(eventDto.getTimeOptionType())
                .timeOptions(new ArrayList<>())
                .restaurantOptions(new ArrayList<>())
                .chat(null)
                .votes(new ArrayList<>())
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

        for (RestaurantOptionDto dto : eventDto.getRestaurantOptions()) {
            RestaurantOption restaurantOption = RestaurantOption.builder()
                    .name(dto.getName())
                    .menuImageUrl(dto.getMenuImageUrl())
                    .restaurantUrl(dto.getRestaurantUrl())
                    .event(newEvent)
                    .votes(new ArrayList<>())
                    .build();
            newEvent.getRestaurantOptions().add(restaurantOption);
        }

        timeOptionEntities.forEach(option -> option.setEvent(newEvent));

        Event savedEvent = eventRepository.save(newEvent);
        return EventMapper.toDto(savedEvent);
    }

    @Override
    public EventDto updateEvent(UpdateEventDto eventDto, Long eventId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        List<Long> participantIds = eventDto.getParticipantIds();
        List<User> users = (participantIds != null && !participantIds.isEmpty())
                ? userService.getUserByIds(participantIds)
                : new ArrayList<>();

        User creator = existingEvent.getCreator();
        if (!users.contains(creator)) {
            users.add(0, creator);
        }

        existingEvent.setTitle(eventDto.getTitle());
        existingEvent.setDescription(eventDto.getDescription());
        existingEvent.setParticipants(users);
        List<TimeOption> timeOptions = (eventDto.getTimeOptions() != null)
                ? eventDto.getTimeOptions().stream()
                .map(dto -> {
                    TimeOption option = TimeOptionMapper.toEntity(dto);
                    option.setEvent(existingEvent);
                    return timeOptionRepository.save(option); // manually save each new TimeOption
                })
                .toList()
                : new ArrayList<>();

        existingEvent.getTimeOptions().clear();
        existingEvent.getTimeOptions().addAll(timeOptions);

        List<RestaurantOption> restaurantOptions = (eventDto.getRestaurantOptions() != null)
                ? eventDto.getRestaurantOptions().stream()
                .map(dto -> {
                    RestaurantOption option = RestaurantOptionMapper.toEntity(dto);
                    option.setEvent(existingEvent);
                    return restaurantOptionRepository.save(option); // manually save each new RestaurantOption
                })
                .toList()
                : new ArrayList<>();

        existingEvent.getRestaurantOptions().clear();
        existingEvent.getRestaurantOptions().addAll(restaurantOptions);

        Event updatedEvent = eventRepository.save(existingEvent);
        return EventMapper.toDto(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public boolean voteForEvent(CreateVote dto, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(dto.getEventId());

        if (userOpt.isEmpty() || eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event or user not found.");
        }

        Optional<Vote> existingVoteOpt = voteRepository.findByUserIdAndEventId(userId, dto.getEventId());

        if (existingVoteOpt.isPresent()) {
            Vote vote = existingVoteOpt.get();

            if (dto.getTimeOptionId() == null || dto.getTimeOptionId() == 0) {
                vote.setTimeOption(null);
            } else if (vote.getTimeOption() == null || vote.getTimeOption().getId() != dto.getTimeOptionId()) {
                Optional<TimeOption> newTimeOpt = timeOptionRepository.findById(dto.getTimeOptionId());
                if (newTimeOpt.isEmpty()) {
                    throw new EventNotFoundException("Time option not found.");
                }
                vote.setTimeOption(newTimeOpt.get());
            }

            if (dto.getRestaurantOptionId() == null) {
                vote.setRestaurantOption(null);
            } else if (dto.getRestaurantOptionId() == 0) {
                vote.setRestaurantOption(null);
            } else {
                Optional<RestaurantOption> restOpt = restaurantOptionRepository.findById(dto.getRestaurantOptionId());
                if (restOpt.isEmpty()) {
                    throw new IllegalArgumentException("No restaurant found with the given ID.");
                }
                vote.setRestaurantOption(restOpt.get());
            }

            voteRepository.save(vote);
            return true;
        }

        Optional<TimeOption> timeOpt = dto.getTimeOptionId() == 0 ? Optional.empty() : timeOptionRepository.findById(dto.getTimeOptionId());
        if (dto.getTimeOptionId() != 0 && timeOpt.isEmpty()) {
            throw new EventNotFoundException("Time option not found.");
        }

        Vote newVote = new Vote();
        newVote.setUser(userOpt.get());
        newVote.setEvent(eventOpt.get());
        newVote.setTimeOption(timeOpt.orElse(null));

        if (dto.getRestaurantOptionId() != null) {
            if (dto.getRestaurantOptionId() == 0) {
                newVote.setRestaurantOption(null);
            } else {
                Optional<RestaurantOption> restOpt = restaurantOptionRepository.findById(dto.getRestaurantOptionId());
                if (restOpt.isEmpty()) {
                    throw new IllegalArgumentException("No restaurant found with the given ID.");
                }
                newVote.setRestaurantOption(restOpt.get());
            }
        }

        voteRepository.save(newVote);
        return true;
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
                .id(user.getId())
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