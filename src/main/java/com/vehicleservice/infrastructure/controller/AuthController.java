package com.vehicleservice.infrastructure.controller;

import com.vehicleservice.application.dto.UserDTO;
import com.vehicleservice.application.dto.request.CreateUserRequest;
import com.vehicleservice.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.get("email"),
                            loginRequest.get("password")));

            if (authentication.isAuthenticated()) {
                var authorities = authentication.getAuthorities()
                        .stream()
                        .map(grantedAuth -> grantedAuth.getAuthority())
                        .collect(Collectors.toList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "roles", authorities));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Login failed: " + ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest registerRequest) {
        try {
            UserDTO savedUser = userService.createUser(registerRequest);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username or Email already exists"));
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(Map.of("message", "Not authenticated"));
        }
        String email = authentication.getName();
        UserDTO user = userService.getUserByEmail(email);

        // Build roles array (even if only one role)
        var authorities = authentication.getAuthorities()
                .stream()
                .map(grantedAuth -> grantedAuth.getAuthority())
                .collect(Collectors.toList());

        // Build a response map
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("roles", authorities); // <-- This is what your frontend expects

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate session and clear authentication
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
} 