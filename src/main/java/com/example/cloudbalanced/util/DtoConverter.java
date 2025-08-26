package com.example.cloudbalanced.util;


import com.example.cloudbalanced.dto.UserCreationDto;
import com.example.cloudbalanced.dto.UserDto;
import com.example.cloudbalanced.model.CloudAccount;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.model.User.UserRole;


import java.util.*;
import java.util.stream.Collectors;
//
//public class DtoConverter {
//
//    public static UserDto convertUserToDto(User user) {
//        // For user creation flow, we might not have assigned accounts yet
//        Set<String> accountIds = Collections.emptySet();
//
//        // Only process assigned accounts if they exist and are not empty
//        if (user.getAssignedAccounts() != null && !user.getAssignedAccounts().isEmpty()) {
//            accountIds = user.getAssignedAccounts().stream()
//                    .map(CloudAccount::getAccountId)
//                    .collect(Collectors.toSet());
//        }
//
//        return new UserDto(
//                user.getId(),
//                user.getUsername(),
//                user.getName(),
//                user.getRole().name(),
//                accountIds,
//                user.getLastActive(),
//                user.getCreatedAt()
//        );
//    }
//
//    public static User convertDtoToUser(UserDto userDto) {
//        User user = new User();
//        user.setId(userDto.getId());
//        user.setUsername(userDto.getUsername());
//        user.setName(userDto.getName());
////        user.setRole(userDto.getRole());
//        user.setRole(UserRole.valueOf(userDto.getRole()));
//        user.setLastActive(userDto.getLastActive());
//        // Note: password and assignedAccounts must be handled separately
//        return user;
//    }
//
//
//
//
//
//
//}


//package com.example.cloudbalanced.util;

import com.example.cloudbalanced.dto.UserCreationDto;
import com.example.cloudbalanced.dto.UserDto;
import com.example.cloudbalanced.model.CloudAccount;
import com.example.cloudbalanced.model.User;
import com.example.cloudbalanced.model.User.UserRole;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoConverter {

    public static UserDto convertUserToDto(User user) {
        Set<String> accountIds = Collections.emptySet();

        if (user.getAssignedAccounts() != null && !user.getAssignedAccounts().isEmpty()) {
            accountIds = user.getAssignedAccounts().stream()
                    .map(CloudAccount::getAccountId)
                    .collect(Collectors.toSet());
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole().name(),
                accountIds,
                user.getLastActive(),
                user.getCreatedAt()
        );
    }

    public static User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setRole(UserRole.valueOf(userDto.getRole()));
        user.setLastActive(userDto.getLastActive());
        return user;
    }

    // ✅ Convert from UserCreationDto → User (with password and assigned CloudAccounts)
    public static User convertUserCreationDtoToUser(UserCreationDto dto, String encodedPassword, Set<CloudAccount> cloudAccounts) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodedPassword); // Already encoded password passed from service layer
        user.setName(dto.getName());
        user.setRole(dto.getRole());

        if (cloudAccounts != null && !cloudAccounts.isEmpty()) {
            user.setAssignedAccounts(cloudAccounts);
        }

        return user;
    }
}


