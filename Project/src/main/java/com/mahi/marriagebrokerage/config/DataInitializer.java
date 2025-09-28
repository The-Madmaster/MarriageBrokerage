package com.mahi.marriagebrokerage.config;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }
    
    private void initializeAdminUser() {
        // Create default admin user
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("TheMadmaster@011"));
            admin.setFullName("System Administrator");
            admin.setRole(User.Role.ADMIN);
            admin.setPhoneNumber("+1-555-ADMIN");
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            System.out.println("✅ Default admin user created successfully!");
            System.out.println("   Email: admin@example.com");
            System.out.println("   Password: TheMadmaster@011");
        } else {
            System.out.println("ℹ️ Admin user already exists in database");
        }
        
        // Create default broker user for testing
        if (!userRepository.existsByEmail("broker@example.com")) {
            User broker = new User();
            broker.setUsername("broker1");
            broker.setEmail("broker@example.com");
            broker.setPassword(passwordEncoder.encode("broker123"));
            broker.setFullName("Demo Broker");
            broker.setRole(User.Role.BROKER);
            broker.setPhoneNumber("+1-555-BROKER");
            broker.setIsActive(true);
            broker.setCreatedAt(LocalDateTime.now());
            broker.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(broker);
            System.out.println("✅ Default broker user created successfully!");
            System.out.println("   Email: broker@example.com");
            System.out.println("   Password: broker123");
        }
    }
}