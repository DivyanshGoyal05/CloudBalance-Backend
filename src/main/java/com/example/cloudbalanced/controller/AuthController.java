package com.example.cloudbalanced.controller;

import com.example.cloudbalanced.dto.JwtRequest;
import com.example.cloudbalanced.dto.JwtResponse;
import com.example.cloudbalanced.dto.RegisterRequest;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.repository.UserRepository;
import com.example.cloudbalanced.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            user.setLastActive(new Date());
            userRepository.save(user);

            return ResponseEntity.ok(new JwtResponse(token, user.getUsername(),
                    user.getRole().toString(), user.getName()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null || request.getName() == null || request.getRole() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username, password, name and role are required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Validate role
        User.UserRole role = request.getRole();
        if (role != User.UserRole.ADMIN && role != User.UserRole.READONLY && role != User.UserRole.CUSTOMER) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(role);
        user.setLastActive(new Date());
        userRepository.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, user.getUsername(),
                user.getRole().toString(), user.getName()));
    }
}
