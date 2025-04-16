package com.example.cloudbalanced.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "cloud_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String accountId;

    @Enumerated(EnumType.STRING)
    private CloudProvider provider;

    private String region;

    @ManyToMany(mappedBy = "assignedAccounts")
    private Set<User> assignedUsers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column
    private String arnRole;

   @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }}



