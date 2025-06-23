package com.example.thesimpleeventapp.controller;


import com.example.thesimpleeventapp.model.Role;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SimpleUserCreator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmail("branislav.mirosavljev01@gmail.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName("Bane");
        user.setLastName("Korisnik");
        user.setRole(Role.USER);
        userRepository.save(user);

        User user2 = new User();
        user2.setEmail("mirosavljev01@gmail.com");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setFirstName("Branislav");
        user2.setLastName("Mirosavljev");
        user2.setRole(Role.ADMIN);
        userRepository.save(user2);

    }
}

