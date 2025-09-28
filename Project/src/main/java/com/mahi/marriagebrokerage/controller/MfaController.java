package com.mahi.marriagebrokerage.controller;

import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import com.mahi.marriagebrokerage.service.MfaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mfa")
@RequiredArgsConstructor
public class MfaController {
    
    private final MfaService mfaService;
    private final UserRepository userRepository;
    
    @PostMapping("/setup-totp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BROKER')")
    public ResponseEntity<?> setupTotp(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            String qrCodeUrl = mfaService.setupTotpForUser(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("qrCodeUrl", qrCodeUrl);
            response.put("message", "Scan the QR code with your authenticator app, then verify with a code to enable MFA");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error setting up MFA: " + e.getMessage());
        }
    }
    
    @PostMapping("/enable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BROKER')")
    public ResponseEntity<?> enableMfa(Authentication authentication, @Valid @RequestBody MfaVerificationRequest request) {
        User user = (User) authentication.getPrincipal();
        
        if (mfaService.enableMfa(user, request.getCode())) {
            return ResponseEntity.ok("MFA enabled successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }
    
    @PostMapping("/disable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BROKER')")
    public ResponseEntity<?> disableMfa(Authentication authentication, @Valid @RequestBody MfaVerificationRequest request) {
        User user = (User) authentication.getPrincipal();
        
        if (mfaService.disableMfa(user, request.getCode())) {
            return ResponseEntity.ok("MFA disabled successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }
    
    @PostMapping("/send-email-otp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BROKER')")
    public ResponseEntity<?> sendEmailOtp(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            mfaService.sendEmailOtp(user);
            return ResponseEntity.ok("OTP sent to your email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending OTP: " + e.getMessage());
        }
    }
    
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BROKER')")
    public ResponseEntity<?> getMfaStatus(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> status = new HashMap<>();
        status.put("mfaEnabled", user.getMfaEnabled() != null && user.getMfaEnabled());
        status.put("hasTotpSecret", user.getTotpSecret() != null);
        status.put("eligible", mfaService.requiresMfa(user) || (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.BROKER));
        
        return ResponseEntity.ok(status);
    }
    
    @Data
    public static class MfaVerificationRequest {
        @NotNull
        private Integer code;
    }
}