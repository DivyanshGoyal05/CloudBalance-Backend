package com.example.cloudbalanced.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String role;
    private Set<String> assignedAccounts;
    private Date lastActive;
    private Date createdAt;
}

