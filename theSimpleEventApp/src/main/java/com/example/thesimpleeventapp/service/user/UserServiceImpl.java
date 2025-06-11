package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDTO;
import com.example.thesimpleeventapp.exception.UserExceptions.EmailAlreadyInUseException;
import com.example.thesimpleeventapp.exception.UserExceptions.PasswordMissmatchException;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.Role;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    private UserRequestDTO convertToDto(User user) {
        return UserRequestDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .profilePicture(user.getProfilePictureUrl())
                .eventsCreated(user.getEventsCreated())
                .notifications(user.getNotifications())
                .build();
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
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
                .eventsCreated(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendUserCreationEmail(savedUser, tempPassword);
        return savedUser;
    }

    @Override
    public void changePassword(
            Long userId,
            PasswordChangeRequestDTO passwordDTO) {
        User user = this.getUserById(userId);

        if (Objects.equals(passwordDTO.getOldPassword(), passwordDTO.getOldPasswordConfirm())
                && passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new PasswordMissmatchException("Passwords don't match");
        }

    }

    @Override
    public List<UserRequestDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateUserProfile(Long userId, UserProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());

        userRepository.save(user);
        return true;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
