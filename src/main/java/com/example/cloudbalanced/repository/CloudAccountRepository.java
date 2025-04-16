package com.example.cloudbalanced.repository;


import com.example.cloudbalanced.model.CloudAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CloudAccountRepository extends JpaRepository<CloudAccount, Long> {
    Optional<CloudAccount> findByAccountId(String accountId);
    
    Optional<CloudAccount> findByArnRole(String arnRole);
    
    @Query(value = "SELECT a FROM CloudAccount a JOIN a.assignedUsers u WHERE u.username = :username")
    List<CloudAccount> findAllByAssignedUser(@Param("username") String username);

}
