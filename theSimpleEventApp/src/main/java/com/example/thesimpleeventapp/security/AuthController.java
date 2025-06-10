package com.example.thesimpleeventapp.security;

import com.example.thesimpleeventapp.dto.auth.AuthResponse;
import com.example.thesimpleeventapp.dto.auth.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.signIn(authRequest);
        if(response.getToken() != null) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(401).body(response);
        }
    }

   
}