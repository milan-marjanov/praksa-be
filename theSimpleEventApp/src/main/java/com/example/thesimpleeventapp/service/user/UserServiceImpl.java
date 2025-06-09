package com.example.thesimpleeventapp.service.user;

import com.example.thesimpleeventapp.exception.UserExceptions.EmailAlreadyInUseException;
import com.example.thesimpleeventapp.exception.UserExceptions.PasswordMissmatchException;
import com.example.thesimpleeventapp.exception.UserExceptions.UserNotFoundException;
import com.example.thesimpleeventapp.model.DTO.UserRequestDTO;
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

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService){
        this.userRepository = userRepository;
        this.emailService = emailService;
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
    public User saveUserWithDefaults(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("User with email " + userRequestDTO.getEmail() + " already exists");
        }
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());

        String tempPassword = UUID.randomUUID().toString();
        user.setPassword(tempPassword);
        user.setRole(Role.USER);
        user.setProfilePictureUrl("https://example.com/default-profile.png");
        user.setEventsCreated(new ArrayList<>());
        user.setNotifications(new ArrayList<>());

        User savedUser = userRepository.save(user);

        emailService.sendUserCreationEmail(savedUser);
        return savedUser;
    }

    @Override
    public void changePassword(
            Long userId,
            String oldPassword,
            String oldPasswordConfirm,
            String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (Objects.equals(oldPassword, oldPasswordConfirm)){
            String hashedPassword = hashPassword(newPassword);
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }else {
            throw new PasswordMissmatchException("Passwords don't match");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
