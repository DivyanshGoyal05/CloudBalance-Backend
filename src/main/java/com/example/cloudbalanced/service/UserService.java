package com.example.cloudbalanced.service;


import com.example.cloudbalanced.dto.UserCreationDto;
import com.example.cloudbalanced.dto.UserDto;
import com.example.cloudbalanced.model.CloudAccount;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.repository.CloudAccountRepository;
import com.example.cloudbalanced.repository.UserRepository;
import com.example.cloudbalanced.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudAccountRepository cloudAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

//         userRepository.findAll().stream()
//                .map(DtoConverter::convertUserToDto)
//                .collect(Collectors.toList());
         List<UserDto> userDtos =  users.stream()
                 .map(DtoConverter::convertUserToDto)
                 .toList();
         return userDtos;

    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(DtoConverter::convertUserToDto);
    }



    //********~~~CREATING THE USER~~~~~************************
    public UserDto createUser(UserCreationDto userCreationDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(userCreationDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userCreationDto.getUsername());
        }

        // Create a new User entity from the DTO
        User user = new User();
        user.setUsername(userCreationDto.getUsername());
        user.setName(userCreationDto.getName());
        user.setRole(userCreationDto.getRole());

        if (userCreationDto.getAssignedAccounts() != null && !userCreationDto.getAssignedAccounts().isEmpty()) {
            Set<CloudAccount> accounts = new HashSet<>(cloudAccountRepository.findAllById(userCreationDto.getAssignedAccounts()));
            user.setAssignedAccounts(accounts);
        }


        // Encode password before saving
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        // Initialize empty assigned accounts
//        user.setAssignedAccounts(Collections.emptySet());

        return DtoConverter.convertUserToDto(userRepository.save(user));
    }

    //*******~~~UPDATING THE USER~~******
    public UserDto updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setUsername(userDetails.getUsername());

        // Only update password if it's not null or empty
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Only admin can change role and assigned accounts
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            user.setRole(userDetails.getRole());

            // Handle account assignments if provided
            if (userDetails.getAssignedAccounts() != null) {
                user.setAssignedAccounts(userDetails.getAssignedAccounts());
            }
        }

        return DtoConverter.convertUserToDto(userRepository.save(user));
    }

//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
    //Not Required

    public boolean isCurrentUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String currentUsername = auth.getName();
            Optional<User> userOpt = userRepository.findById(userId);
            return userOpt.isPresent() && userOpt.get().getUsername().equals(currentUsername);
        }
        return false;
    }
}
