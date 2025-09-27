package com.mahi.marriagebrokerage.controller;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/brokers")
    public ResponseEntity<?> getAllBrokers() {
        List<User> brokers = userRepository.findByRole(User.Role.BROKER);
        return ResponseEntity.ok(brokers);
    }
}
