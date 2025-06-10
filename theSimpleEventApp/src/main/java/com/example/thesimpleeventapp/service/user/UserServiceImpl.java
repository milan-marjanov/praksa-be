package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.dto.UserRequestDTO;
import com.example.thesimpleeventapp.exception.UserExceptions.EmailAlreadyInUseException;
import com.example.thesimpleeventapp.exception.UserExceptions.PasswordMissmatchException;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.dto.PasswordChangeRequestDTO;
import com.example.thesimpleeventapp.dto.CreateUserDTO;
import com.example.thesimpleeventapp.model.Role;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.UserRepository;
import com.example.thesimpleeventapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService){
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private UserRequestDTO convertToDto(User user){
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

    public String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex string
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public User saveUserWithDefaults(CreateUserDTO createUserDTO) {
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("User with email " + createUserDTO.getEmail() + " already exists");
        }

        String tempPassword = UUID.randomUUID().toString();
        User user = User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .email(createUserDTO.getEmail())
                .password(tempPassword)
                .role(Role.USER)
                .profilePictureUrl("https://example.com/default-profile.png")
                .eventsCreated(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        emailService.sendUserCreationEmail(savedUser);
        return savedUser;
    }

    @Override
    public void changePassword(
            Long userId,
            PasswordChangeRequestDTO passwordDTO) {
        User user = this.getUserById(userId);

        if (Objects.equals(passwordDTO.getOldPassword(), passwordDTO.getOldPasswordConfirm())){
            String hashedPassword = hashPassword(passwordDTO.getNewPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }else {
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
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
