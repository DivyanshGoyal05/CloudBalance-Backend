//package com.example.cloudbalanced.config;
//
////public class DataLoader {
////}
//
//
//import com.example.cloudbalanced.model.User;
//import com.example.cloudbalanced.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//import java.util.Date;
//
//@Configuration
//public class DataLoader {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Bean
//    CommandLineRunner initUsers(UserRepository userRepository) {
//        return args -> {
//            // Create sample users
//            User adminUser = new User();
//            adminUser.setUsername("admin");
//            adminUser.setPassword(passwordEncoder.encode("admin123"));
//            adminUser.setName("Admin User");
//            adminUser.setRole(User.UserRole.ADMIN);
//            adminUser.setLastActive(new Date());
//
//            User readonlyUser = new User();
//            readonlyUser.setUsername("readonly");
//            readonlyUser.setPassword(passwordEncoder.encode("readonly123"));
//            readonlyUser.setName("Read Only User");
//            readonlyUser.setRole(User.UserRole.READONLY);
//            readonlyUser.setLastActive(new Date());
//
//            User customerUser = new User();
//            customerUser.setUsername("customer");
//            customerUser.setPassword(passwordEncoder.encode("customer123"));
//            customerUser.setName("Customer User");
//            customerUser.setRole(User.UserRole.CUSTOMER);
//            customerUser.setLastActive(new Date());
//
//            userRepository.saveAll(Arrays.asList(adminUser, readonlyUser, customerUser));
//
//            System.out.println("Sample users inserted successfully.");
//        };
//    }
//}