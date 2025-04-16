package com.example.cloudbalanced.controller;

import com.example.cloudbalanced.dto.JwtRequest;
import com.example.cloudbalanced.dto.JwtResponse;
import com.example.cloudbalanced.dto.UserDto;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.repository.UserRepository;
import com.example.cloudbalanced.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

//@CrossOrigin(origins = "https://localhost:3000")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
//        System.out.println("DATA"+request);
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                    //why we are writing new?
            );

            // Generate JWT token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Get user details
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            // Update last active timestamp
            user.setLastActive(new Date());
            userRepository.save(user);

            return ResponseEntity.ok(new JwtResponse(token, user.getUsername(),
                    user.getRole().toString(), user.getName()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
