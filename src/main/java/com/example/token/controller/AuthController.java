package com.example.token.controller;

import com.example.token.model.LoginRequest;
import com.example.token.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return authService.generateToken(request.getUsername());
            } else {
                throw new RuntimeException("Authentication failed");
            }

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @GetMapping("/username/{token}")
    public String extractUsername(@PathVariable String token) {
        return authService.extractUsername(token);
    }

    @GetMapping("/user")
    public String getUser(Authentication authentication) {
        return "User: " + authentication.getName();
    }

    @GetMapping("/admin")
    public String onlyAdmin(Authentication authentication) {
        return "Admin: " + authentication.getName();
    }
}
