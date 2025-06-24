package com.example.thesimpleeventapp.service.event;

import com.example.thesimpleeventapp.dto.event.*;
import com.example.thesimpleeventapp.dto.mapper.EventMapper;
import com.example.thesimpleeventapp.dto.mapper.RestaurantOptionMapper;
import com.example.thesimpleeventapp.dto.mapper.TimeOptionMapper;
import com.example.thesimpleeventapp.dto.mapper.VoteMapper;
import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import com.example.thesimpleeventapp.dto.vote.CreateVote;
import com.example.thesimpleeventapp.dto.vote.VoteDto;
import com.example.thesimpleeventapp.exception.EventExceptions.EventNotFoundException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidEventDataException;
import com.example.thesimpleeventapp.exception.EventExceptions.InvalidTimeOptionException;
import com.example.thesimpleeventapp.exception.VoteExceptions.NotFoundException;
import com.example.thesimpleeventapp.exception.VoteExceptions.TimeExpiredException;
import com.example.thesimpleeventapp.model.*;
import com.example.thesimpleeventapp.repository.*;
import com.example.thesimpleeventapp.service.notification.NotificationService;
import com.example.thesimpleeventapp.service.notification.VotingReminderService;
import com.example.thesimpleeventapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TimeOptionRepository timeOptionRepository;
    private final RestaurantOptionRepository restaurantOptionRepository;
    private final VoteRepository voteRepository;
    private final NotificationService notificationService;
    private final VotingReminderService votingReminderService;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            UserService userService,
                            UserRepository userRepository,
                            TimeOptionRepository timeOptionRepository,
                            RestaurantOptionRepository restaurantOptionRepository,
                            VoteRepository voteRepository,
                            NotificationService notificationService,
                            VotingReminderService votingReminderService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.timeOptionRepository = timeOptionRepository;
        this.restaurantOptionRepository = restaurantOptionRepository;
        this.voteRepository = voteRepository;
        this.notificationService = notificationService;
        this.votingReminderService = votingReminderService;
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

    private void processTimeOptions(List<TimeOptionDto> timeOptionDtos, Event event) {
        if (timeOptionDtos != null && !timeOptionDtos.isEmpty()) {
            validateTimeOptions(event.getTimeOptionType(), timeOptionDtos);

            List<TimeOption> timeOptions = timeOptionDtos.stream()
                    .map(TimeOptionMapper::toEntity)
                    .peek(option -> option.setEvent(event))
                    .toList();

            event.getTimeOptions().addAll(timeOptions);

            for (TimeOption option : timeOptions) {
                timeOptionRepository.save(option);
            }
        }
    }

    private void processRestaurantOptions(List<RestaurantOptionDto> restaurantOptionDtos, Event event) {
        if (restaurantOptionDtos != null && !restaurantOptionDtos.isEmpty()) {
            for (RestaurantOptionDto dto : restaurantOptionDtos) {
                RestaurantOption option = RestaurantOption.builder()
                        .name(dto.getName())
                        .menuImageUrl(dto.getMenuImageUrl())
                        .restaurantUrl(dto.getRestaurantUrl())
                        .event(event)
                        .votes(new ArrayList<>())
                        .build();

                event.getRestaurantOptions().add(option);

                restaurantOptionRepository.save(option);
            }
        }
    }

    private void notifyUsersAboutEvent(String title, String text, List<User> users, Event updatedEvent) {
        for (User participant : users) {
            notificationService.createNotification(
                    title,
                    text,
                    updatedEvent,
                    participant
            );
        }
    }

    @Override
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
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
                .votingDeadline(eventDto.getVotingDeadline())
                .timeOptionType(eventDto.getTimeOptionType())
                .timeOptions(new ArrayList<>())
                .restaurantOptionType(eventDto.getRestaurantOptionType())
                .restaurantOptions(new ArrayList<>())
                .chat(null)
                .votes(new ArrayList<>())
                .votingDeadline(eventDto.getVotingDeadline())
                .build();
        Event savedEvent = eventRepository.save(newEvent);

        processTimeOptions(eventDto.getTimeOptions(), newEvent);
        processRestaurantOptions(eventDto.getRestaurantOptions(), newEvent);

        notifyUsersAboutEvent("Event creation",
                "You have been invited to event: " + eventDto.getTitle(),
                initialParticipants,
                newEvent);

        votingReminderService.scheduleVotingReminder(savedEvent);

        processTimeOptions(eventDto.getTimeOptions(), newEvent);
        processRestaurantOptions(eventDto.getRestaurantOptions(), newEvent);

        return EventMapper.toDto(savedEvent);
    }

    @Override
    public EventDto updateEvent(UpdateEventDto eventDto, Long eventId) {
        Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        List<User> users = Optional.ofNullable(eventDto.getParticipantIds())
                .filter(list -> !list.isEmpty())
                .map(userService::getUserByIds)
                .orElse(new ArrayList<>());
        if (!users.contains(existing.getCreator())) {
            users.add(0, existing.getCreator());
        }
        existing.setTitle(eventDto.getTitle());
        existing.setDescription(eventDto.getDescription());
        existing.setTimeOptionType(eventDto.getTimeOptionType());
        existing.setRestaurantOptionType(eventDto.getRestaurantOptionType());
        existing.setParticipants(users);
        existing.setVotingDeadline(eventDto.getVotingDeadline());

        Map<Long, TimeOption> existingTimeMap = existing.getTimeOptions().stream()
                .collect(Collectors.toMap(TimeOption::getId, Function.identity()));
        List<TimeOption> mergedTimes = new ArrayList<>();
        if (eventDto.getTimeOptions() != null) {
            for (TimeOptionDto dto : eventDto.getTimeOptions()) {
                if (dto.getId() != null && existingTimeMap.containsKey(dto.getId())) {
                    TimeOption opt = existingTimeMap.remove(dto.getId());
                    opt.setStartTime(dto.getStartTime());
                    opt.setEndTime(dto.getEndTime());
                    opt.setMaxCapacity(dto.getMaxCapacity());
                    mergedTimes.add(opt);
                } else {
                    TimeOption opt = TimeOptionMapper.toEntity(dto);
                    opt.setEvent(existing);
                    mergedTimes.add(opt);
                }
            }
        }
        existing.getTimeOptions().clear();
        existing.getTimeOptions().addAll(mergedTimes);

        Map<Long, RestaurantOption> existingRestMap = existing.getRestaurantOptions().stream()
                .collect(Collectors.toMap(RestaurantOption::getId, Function.identity()));
        List<RestaurantOption> mergedRests = new ArrayList<>();
        if (eventDto.getRestaurantOptions() != null) {
            for (RestaurantOptionDto dto : eventDto.getRestaurantOptions()) {
                if (dto.getId() != null && existingRestMap.containsKey(dto.getId())) {
                    RestaurantOption opt = existingRestMap.remove(dto.getId());
                    opt.setName(dto.getName());
                    opt.setMenuImageUrl(dto.getMenuImageUrl());
                    opt.setRestaurantUrl(dto.getRestaurantUrl());
                    mergedRests.add(opt);
                } else {
                    RestaurantOption opt = RestaurantOptionMapper.toEntity(dto);
                    opt.setEvent(existing);
                    mergedRests.add(opt);
                }
            }
        }
        existing.getRestaurantOptions().clear();
        existing.getRestaurantOptions().addAll(mergedRests);
        Event updatedEvent = eventRepository.save(existing);
        notifyUsersAboutEvent("Event update", "An event has been updated", users, updatedEvent);

        return EventMapper.toDto(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public VoteDto voteForEvent(CreateVote dto, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(dto.getEventId());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDeadline = eventOpt.get().getVotingDeadline();

        if (eventDeadline.isBefore(now)) {
            throw new TimeExpiredException("Time expired");
        }

        Optional<Vote> existingVoteOpt = voteRepository.findByUserIdAndEventId(userId, dto.getEventId());
        if (existingVoteOpt.isPresent()) {
            Vote vote = existingVoteOpt.get();

            if (dto.getTimeOptionId() == null) {
                vote.setTimeOption(null);
            } else if (vote.getTimeOption() == null || vote.getTimeOption().getId() != dto.getTimeOptionId()) {
                TimeOption to = timeOptionRepository
                        .findById(dto.getTimeOptionId())
                        .orElseThrow(() -> new EventNotFoundException("Time option not found."));
                vote.setTimeOption(to);
            }

            if (dto.getRestaurantOptionId() == null) {
                vote.setRestaurantOption(null);
            } else {
                RestaurantOption ro = restaurantOptionRepository
                        .findById(dto.getRestaurantOptionId())
                        .orElseThrow(() -> new EventNotFoundException("Restaurant option not found."));
                vote.setRestaurantOption(ro);
            }


            if (vote.getTimeOption() == null && vote.getRestaurantOption() == null) {

                voteRepository.delete(vote);
                return VoteMapper.toDto(vote);

            } else {
                Vote saved = voteRepository.save(vote);
                return VoteMapper.toDto(saved);
            }

        }

        if (dto.getTimeOptionId() == null && dto.getRestaurantOptionId() == null) {
            throw new NotFoundException("Bad Request");
        }

        Vote newVote = new Vote();
        newVote.setUser(userOpt.get());
        newVote.setEvent(eventOpt.get());

        if (dto.getTimeOptionId() != null) {
            Optional<TimeOption> timeOpt = timeOptionRepository.findById(dto.getTimeOptionId());
            if (timeOpt.isEmpty()) {
                throw new NotFoundException("Time option not found.");
            }
            newVote.setTimeOption(timeOpt.get());
        } else {
            newVote.setTimeOption(null);
        }


        if (dto.getRestaurantOptionId() != null) {
            Optional<RestaurantOption> restaurantOption = restaurantOptionRepository.findById(dto.getRestaurantOptionId());
            if (restaurantOption.isEmpty()) {
                throw new NotFoundException("Time option not found.");
            }
            newVote.setRestaurantOption(restaurantOption.get());
        } else {
            newVote.setRestaurantOption(null);
        }

        Vote savedNew = voteRepository.save(newVote);
        return VoteMapper.toDto(savedNew);
    }

    @Override
    public List<EventBasicDto> getAllBasicEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::convertToBasicDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDetailsDto getEventDetails(long id, long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        List<UserProfileDto> participants = event.getParticipants().stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());

        List<TimeOptionDto> timeOptions = event.getTimeOptions().stream()
                .map(this::convertTimeOptionToDto)
                .collect(Collectors.toList());

        List<RestaurantOptionDto> restaurantOptions = event.getRestaurantOptions().stream()
                .map(this::convertRestaurantToDto)
                .collect(Collectors.toList());

        EventDetailsDto dto = EventDetailsDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .participants(participants)
                .timeOptions(timeOptions)
                .restaurantOptions(restaurantOptions)
                .timeOptionType(event.getTimeOptionType())
                .restaurantOptionType(event.getRestaurantOptionType())
                .currentVote(null)
                .build();

        List<Vote> allVotes = voteRepository.findByEventId(id);

        Map<Long, List<Vote>> votesByTime = allVotes.stream()
                .filter(v -> v.getTimeOption() != null)
                .collect(Collectors.groupingBy(v -> v.getTimeOption().getId()));

        for (TimeOptionDto to : dto.getTimeOptions()) {
            List<Vote> vs = votesByTime.getOrDefault(to.getId(), List.of());
            to.setVotesCount(vs.size());
            to.setReservedCount(vs.size());
            to.setVotedUsers(
                    vs.stream()
                            .map(v -> convertUserToDto(v.getUser()))
                            .collect(Collectors.toList())
            );
        }

        Map<Long, List<Vote>> votesByRest = allVotes.stream()
                .filter(v -> v.getRestaurantOption() != null)
                .collect(Collectors.groupingBy(v -> v.getRestaurantOption().getId()));

        for (RestaurantOptionDto ro : dto.getRestaurantOptions()) {
            List<Vote> vs = votesByRest.getOrDefault(ro.getId(), List.of());
            ro.setVotesCount(vs.size());
            ro.setVotedUsers(
                    vs.stream()
                            .map(v -> convertUserToDto(v.getUser()))
                            .collect(Collectors.toList())
            );
        }

        Optional<Vote> existing = voteRepository.findByUserIdAndEventId(userId, id);
        dto.setCurrentVote(
                existing.map(VoteMapper::toDto).orElse(null)
        );

        return dto;
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
                .createdAt(timeOption.getCreatedAt())
                .maxCapacity(timeOption.getMaxCapacity())
                .votesCount(0)
                .reservedCount(0)
                .votedUsers(new ArrayList<>())
                .build();
    }

    private RestaurantOptionDto convertRestaurantToDto(RestaurantOption restaurantOption) {
        return RestaurantOptionDto.builder()
                .id(restaurantOption.getId())
                .name(restaurantOption.getName())
                .menuImageUrl(restaurantOption.getMenuImageUrl())
                .restaurantUrl(restaurantOption.getRestaurantUrl())
                .votesCount(0)
                .votedUsers(new ArrayList<>())
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
