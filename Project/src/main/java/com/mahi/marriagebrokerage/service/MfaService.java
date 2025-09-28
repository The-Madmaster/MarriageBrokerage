package com.mahi.marriagebrokerage.service;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class MfaService {
    
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final GoogleAuthenticator googleAuthenticator;
    
    @Value("${app.name}")
    private String appName;
    
    @Value("${spring.mail.username:noreply@marriagebrokerage.com}")
    private String fromEmail;
    
    // Store temporary OTP codes (in production, use Redis or similar)
    private final ConcurrentHashMap<String, String> emailOtpStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> otpExpiryStore = new ConcurrentHashMap<>();
    
    @Transactional
    public String setupTotpForUser(User user) {
        if (!isMfaEligible(user)) {
            throw new IllegalStateException("MFA is only available for Admin and Broker users");
        }
        
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secret = key.getKey();
        
        user.setTotpSecret(secret);
        userRepository.save(user);
        
        // Generate QR code URL
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(appName, user.getEmail(), key);
    }
    
    @Transactional
    public boolean enableMfa(User user, int totpCode) {
        if (!isMfaEligible(user)) {
            return false;
        }
        
        if (user.getTotpSecret() == null) {
            return false;
        }
        
        if (googleAuthenticator.authorize(user.getTotpSecret(), totpCode)) {
            user.setMfaEnabled(true);
            userRepository.save(user);
            log.info("MFA enabled for user: {}", user.getUsername());
            return true;
        }
        return false;
    }
    
    @Transactional
    public boolean disableMfa(User user, int totpCode) {
        if (!user.getMfaEnabled()) {
            return false;
        }
        
        if (googleAuthenticator.authorize(user.getTotpSecret(), totpCode)) {
            user.setMfaEnabled(false);
            user.setTotpSecret(null);
            userRepository.save(user);
            log.info("MFA disabled for user: {}", user.getUsername());
            return true;
        }
        return false;
    }
    
    public boolean validateTotp(User user, int totpCode) {
        if (!user.getMfaEnabled() || user.getTotpSecret() == null) {
            return false;
        }
        
        return googleAuthenticator.authorize(user.getTotpSecret(), totpCode);
    }
    
    public void sendEmailOtp(User user) {
        if (!isMfaEligible(user)) {
            throw new IllegalStateException("MFA is only available for Admin and Broker users");
        }
        
        String otp = generateEmailOtp();
        String key = user.getEmail();
        
        emailOtpStore.put(key, otp);
        otpExpiryStore.put(key, LocalDateTime.now().plusMinutes(5)); // 5 minute expiry
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Your " + appName + " Login Code");
            message.setText("Your login verification code is: " + otp + "\n\nThis code will expire in 5 minutes.");
            
            mailSender.send(message);
            log.info("Email OTP sent to user: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to send email OTP to user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to send OTP email");
        }
    }
    
    public boolean validateEmailOtp(User user, String otp) {
        String key = user.getEmail();
        String storedOtp = emailOtpStore.get(key);
        LocalDateTime expiry = otpExpiryStore.get(key);
        
        if (storedOtp == null || expiry == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(expiry)) {
            emailOtpStore.remove(key);
            otpExpiryStore.remove(key);
            return false;
        }
        
        boolean valid = storedOtp.equals(otp);
        if (valid) {
            emailOtpStore.remove(key);
            otpExpiryStore.remove(key);
        }
        
        return valid;
    }
    
    public boolean requiresMfa(User user) {
        return isMfaEligible(user) && (user.getMfaEnabled() != null && user.getMfaEnabled());
    }
    
    private boolean isMfaEligible(User user) {
        return user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.BROKER;
    }
    
    private String generateEmailOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}