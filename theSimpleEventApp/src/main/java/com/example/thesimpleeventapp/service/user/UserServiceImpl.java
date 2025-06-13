package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.user.*;
import com.example.thesimpleeventapp.dto.mapper.UserMapper;
import com.example.thesimpleeventapp.dto.user.CreateUserDto;
import com.example.thesimpleeventapp.dto.user.PasswordChangeRequestDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDto;
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

import java.util.List;
import java.util.UUID;
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
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    private UserPublicProfileDto convertToPublicProfileDto(User user) {
        return UserPublicProfileDto.builder()
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
    public void updateProfilePicture(Long userId, ProfilePictureUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        userRepository.save(user);
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
        user.setProfilePictureUrl(dto.getProfilePictureUrl());

        userRepository.save(user);
        return convertToPersonalDtoProfile(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
