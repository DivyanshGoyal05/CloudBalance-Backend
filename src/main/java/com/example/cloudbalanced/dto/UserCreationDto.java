
package com.example.cloudbalanced.dto;


import com.example.cloudbalanced.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDto {
    private String username;
    private String password;
    private String name;
    private User.UserRole role;
    private List<Long> assignedAccounts;
}
