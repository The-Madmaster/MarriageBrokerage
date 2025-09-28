package com.mahi.marriagebrokerage.service;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLockoutService {
    
    private final UserRepository userRepository;
    
    @Value("${security.max-failed-attempts:5}")
    private int maxFailedAttempts;
    
    @Value("${security.lockout-duration-minutes:30}")
    private int lockoutDurationMinutes;
    
    @Transactional
    public void recordFailedAttempt(User user) {
        int newFailedAttempts = (user.getFailedAttempts() != null ? user.getFailedAttempts() : 0) + 1;
        user.setFailedAttempts(newFailedAttempts);
        
        if (newFailedAttempts >= maxFailedAttempts) {
            user.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
            log.warn("Account locked for user: {} after {} failed attempts", user.getUsername(), newFailedAttempts);
        }
        
        userRepository.save(user);
    }
    
    @Transactional
    public void recordSuccessfulLogin(User user) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public boolean isAccountLocked(User user) {
        return user.getLockedUntil() != null && LocalDateTime.now().isBefore(user.getLockedUntil());
    }
    
    @Transactional
    public void unlockAccount(User user) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
        log.info("Account unlocked for user: {}", user.getUsername());
    }
    
    public LocalDateTime getLockoutExpiry(User user) {
        return user.getLockedUntil();
    }
}