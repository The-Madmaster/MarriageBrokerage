package com.mahi.marriagebrokerage.dto.request;

import com.mahi.marriagebrokerage.entity.Client;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientRegistrationRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotNull(message = "Gender is required")
    private Client.Gender gender;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    private String occupation;
    private String education;
    private String religion;
    private String caste;
    private String subcaste;
    
    @PositiveOrZero(message = "Annual income must be positive")
    private Double annualIncome;
    
    private String city;
    private String state;
    private String country;
    
    @Min(value = 100, message = "Height must be at least 100 cm")
    @Max(value = 250, message = "Height must be at most 250 cm")
    private Integer heightCm;
    
    @Min(value = 30, message = "Weight must be at least 30 kg")
    @Max(value = 200, message = "Weight must be at most 200 kg")
    private Integer weightKg;
    
    @NotNull(message = "Marital status is required")
    private Client.MaritalStatus maritalStatus;
    
    // Family Information
    private String fatherName;
    private String fatherOccupation;
    private String motherName;
    private String motherOccupation;
    private String familyType;
    
    @PositiveOrZero(message = "Family income must be positive")
    private Double familyIncome;
    
    @Min(value = 0, message = "Siblings count cannot be negative")
    private Integer siblingsCount;
    
    // Preferences
    @Min(value = 18, message = "Preferred minimum age must be at least 18")
    private Integer preferredAgeMin;
    
    @Max(value = 100, message = "Preferred maximum age must be at most 100")
    private Integer preferredAgeMax;
    
    private Integer preferredHeightMin;
    private Integer preferredHeightMax;
    private String preferredReligion;
    private String preferredCaste;
    private String preferredEducation;
    private String preferredOccupation;
    
    @PositiveOrZero(message = "Preferred minimum income must be positive")
    private Double preferredIncomeMin;
    
    private String preferredLocation;
    
    // Profile
    private String profilePhotoUrl;
    private String horoscopeUrl;
    
    @Size(max = 1000, message = "Bio must be at most 1000 characters")
    private String bio;
    
    @NotNull(message = "Broker ID is required")
    private Long brokerId;
}