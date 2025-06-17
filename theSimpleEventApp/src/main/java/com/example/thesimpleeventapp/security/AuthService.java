package com.example.thesimpleeventapp.security;


import com.example.thesimpleeventapp.dto.auth.AuthRequest;
import com.example.thesimpleeventapp.dto.auth.AuthResponse;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse signIn(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
            String jwt = jwtUtils.generateToken(user);

            response.setToken(jwt);
            response.setMessage("Successfully signed in");
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

}
