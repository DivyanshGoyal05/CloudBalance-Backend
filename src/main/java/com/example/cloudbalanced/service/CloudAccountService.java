package com.example.cloudbalanced.service;


import com.example.cloudbalanced.dto.CloudAccountDto;
import com.example.cloudbalanced.model.CloudAccount;
import com.example.cloudbalanced.repository.CloudAccountRepository;
import com.example.cloudbalanced.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.cloudbalanced.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CloudAccountService {
    @Autowired
    private CloudAccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CloudAccountDto> getAllAccountsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

//         For admin users, return all accounts
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            List<CloudAccount> cloudAccounts = accountRepository.findAll();
            List<CloudAccountDto> cloudAccountDtos =
                    cloudAccounts.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return cloudAccountDtos;
        }

        // For other users, return only assigned accounts
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get().getAssignedAccounts().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public Optional<CloudAccountDto> getAccountById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<CloudAccount> accountOpt = accountRepository.findById(id);

        // Check if account exists and user has access
        if (accountOpt.isPresent()) {
            CloudAccount account = accountOpt.get();

            // Admin can access any account
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return accountOpt.map(this::convertToDto);
            }

            // Check if user is assigned to this account
            boolean hasAccess = account.getAssignedUsers().stream()
                    .anyMatch(user -> user.getUsername().equals(username));

            if (hasAccess) {
                return accountOpt.map(this::convertToDto);
            }
        }

        return Optional.empty();
    }

    public CloudAccountDto createAccount(CloudAccount account) {
        return convertToDto(accountRepository.save(account));
    }

    public CloudAccountDto updateAccount(Long id, CloudAccount accountDetails) {
        CloudAccount account = accountRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Cloud Account not found with id: " + id));

        account.setName(accountDetails.getName());
        account.setAccountId(accountDetails.getAccountId());
        account.setProvider(accountDetails.getProvider());
        account.setRegion(accountDetails.getRegion());
        account.setStatus(accountDetails.getStatus());
        account.setArnRole(accountDetails.getArnRole());

        // Handle user assignments if provided
        if (accountDetails.getAssignedUsers() != null) {
            account.setAssignedUsers(accountDetails.getAssignedUsers());
        }

        return convertToDto(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private CloudAccountDto convertToDto(CloudAccount account) {
        Set<String> assignedUsernames = account.getAssignedUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        return new CloudAccountDto(
                account.getId(),
                account.getName(),
                account.getAccountId(),
                account.getProvider(),
                account.getRegion(),
                account.getArnRole(),
                assignedUsernames,
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}

