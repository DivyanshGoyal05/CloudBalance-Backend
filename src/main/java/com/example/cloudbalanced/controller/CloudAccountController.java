package com.example.cloudbalanced.controller;

//public class CloudAccountController {
//}

import com.example.cloudbalanced.dto.CloudAccountDto;
import com.example.cloudbalanced.model.CloudAccount;
import com.example.cloudbalanced.service.CloudAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloudAccount")
public class CloudAccountController {
    @Autowired
    private CloudAccountService accountService;

    @GetMapping("/getall")
    public ResponseEntity<List<CloudAccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccountsForCurrentUser());
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable(name="id") Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudAccountDto> createAccount(@RequestBody CloudAccount account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudAccountDto> updateAccount(@PathVariable Long id, @RequestBody CloudAccount account) {
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}

