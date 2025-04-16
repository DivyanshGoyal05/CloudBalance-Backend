package com.example.cloudbalanced.controller;


import com.example.cloudbalanced.dto.JwtRequest;
import com.example.cloudbalanced.dto.UserCreationDto;
import com.example.cloudbalanced.dto.UserDto;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.repository.UserRepository;
import com.example.cloudbalanced.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



   @RestController
   @RequestMapping("/users")
   public class UserController {
      @Autowired
      private UserService userService;

      @GetMapping("/all")
//      @PreAuthorize("hasRole('ADMIN')")
      public ResponseEntity<List<UserDto>> getAllUsers() {
         return ResponseEntity.ok(userService.getAllUsers());
      }

      @GetMapping("/{id}")
      @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
      public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
         return userService.getUserById(id)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
      }

      @PostMapping("/create")
//      @PreAuthorize("hasRole('ADMIN')")
      public ResponseEntity<?> createUser(@RequestBody UserCreationDto userCreationDto) {
         try {
            return ResponseEntity.ok(userService.createUser(userCreationDto));
         } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
         }
      }

      @PutMapping("/{id}")
      @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
      public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User user) {
         return ResponseEntity.ok(userService.updateUser(id, user));
      }

//      @DeleteMapping("/{id}")
//      @PreAuthorize("hasRole('ADMIN')")
//      public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//         userService.deleteUser(id);
//         return ResponseEntity.ok().build();
//      }
   }