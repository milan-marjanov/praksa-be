package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.mapper.UserMapper;
import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.exception.UserExceptions.EmailAlreadyInUseException;
import com.example.thesimpleeventapp.exception.UserExceptions.PasswordMissmatchException;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.Chat;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.Role;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.EventRepository;
import com.example.thesimpleeventapp.repository.MessageRepository;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.repository.VoteRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import com.example.thesimpleeventapp.service.event.EventService;
import jakarta.transaction.Transactional;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String UPLOAD_DIR = "images/";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VoteRepository voteRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final MessageRepository messageRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, VoteRepository voteRepository, EventRepository eventRepository, @Lazy EventService eventService, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.voteRepository = voteRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.messageRepository = messageRepository;

    }

    private UserRequestDto convertToDto(User user) {
        return UserRequestDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .profilePicture(user.getProfilePictureUrl())
                .build();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public UserRequestDto getUserDtoById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return UserMapper.userRequestToDto(user);
    }

    @Override
    public List<User> getUserByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User saveUserWithDefaults(CreateUserDto createUserDTO) {
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("User with email " + createUserDTO.getEmail() + " already exists");
        }

        String tempPassword = UUID.randomUUID().toString();
        User user = User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(tempPassword))
                .role(Role.USER)
                .profilePictureUrl("https://example.com/default-profile.png")
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendUserCreationEmail(savedUser, tempPassword);
        return savedUser;
    }

    @Override
    public void changePassword(Long userId, PasswordChangeRequestDto passwordDTO) {
        User user = this.getUserById(userId);

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordMissmatchException("Old password is incorrect");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getNewPasswordConfirm())) {
            throw new PasswordMissmatchException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserProfileDto getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return convertToPersonalDtoProfile(user);


    }

    private UserProfileDto convertToPersonalDtoProfile(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private UserPublicProfileDto convertToPublicProfileDto(User user) {
        return UserPublicProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    @Override
    public UserPublicProfileDto getPublicProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return convertToPublicProfileDto(user);
    }

    @Override
    public List<UserRequestDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto updateUserProfile(Long userId, UpdateUserProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());


        userRepository.save(user);
        return convertToPersonalDtoProfile(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Event> createdEvents = eventRepository.findByCreator(user);
        for (Event event : createdEvents) {
            eventRepository.delete(event);
        }
        List<Event> participatingEvents = eventRepository.findByParticipantsContaining(user);

        for (Event event : participatingEvents) {
            Chat chat = event.getChat();
            if (chat != null && chat.getMessages() != null) {
                chat.getMessages().removeIf(message -> message.getUser().equals(user));
            }
            event.getParticipants().remove(user);
        }

        voteRepository.deleteByUserId(userId);
        messageRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

    @Override
    public String saveProfilePicture(Long userId, MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + userId));

        try {

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String imageUrl = "user_" + userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(UPLOAD_DIR, imageUrl).normalize();
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfilePictureUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);

        }

    }

    @Override
    public ResponseEntity<Resource> loadImage(Long userId) throws MalformedURLException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String filename = user.getProfilePictureUrl();
        if (filename == null || filename.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            MediaType contentType = MediaTypeFactory.getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(resource);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @Override
    public void deleteImage(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setProfilePictureUrl(null);
        userRepository.save(user);
    }
}
