package com.mahi.marriagebrokerage.config;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        // Create admin user if doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("TheMadmaster@011"));
            admin.setEmail("admin@marriagebrokerage.com");
            admin.setFullName("System Administrator");
            admin.setPhoneNumber("+1234567890");
            admin.setRole(User.Role.ADMIN);
            admin.setIsActive(true);
            
            userRepository.save(admin);
            log.info("Created default admin user: admin");
        }
        
        // Create broker user if doesn't exist
        if (!userRepository.existsByUsername("broker1")) {
            User broker = new User();
            broker.setUsername("broker1");
            broker.setPassword(passwordEncoder.encode("broker123"));
            broker.setEmail("broker1@marriagebrokerage.com");
            broker.setFullName("John Broker");
            broker.setPhoneNumber("+1234567891");
            broker.setRole(User.Role.BROKER);
            broker.setIsActive(true);
            
            userRepository.save(broker);
            log.info("Created default broker user: broker1");
        }
        
        // Create client user if doesn't exist
        if (!userRepository.existsByUsername("client1")) {
            User client = new User();
            client.setUsername("client1");
            client.setPassword(passwordEncoder.encode("client123"));
            client.setEmail("client1@marriagebrokerage.com");
            client.setFullName("Jane Client");
            client.setPhoneNumber("+1234567892");
            client.setRole(User.Role.CLIENT);
            client.setIsActive(true);
            
            userRepository.save(client);
            log.info("Created default client user: client1");
        }
        
        log.info("Data initialization completed successfully");
    }
}