package com.mahi.marriagebrokerage.dto.response;

import com.mahi.marriagebrokerage.entity.Client;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClientResponse {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Client.Gender gender;
    private String phoneNumber;
    private String email;
    private String occupation;
    private String education;
    private String religion;
    private String caste;
    private String subcaste;
    private Double annualIncome;
    private String city;
    private String state;
    private String country;
    private Integer heightCm;
    private Integer weightKg;
    private Client.MaritalStatus maritalStatus;
    
    // Family Information
    private String fatherName;
    private String fatherOccupation;
    private String motherName;
    private String motherOccupation;
    private String familyType;
    private Double familyIncome;
    private Integer siblingsCount;
    
    // Preferences
    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private Integer preferredHeightMin;
    private Integer preferredHeightMax;
    private String preferredReligion;
    private String preferredCaste;
    private String preferredEducation;
    private String preferredOccupation;
    private Double preferredIncomeMin;
    private String preferredLocation;
    
    // Profile
    private String profilePhotoUrl;
    private String horoscopeUrl;
    private String bio;
    private Boolean isActive;
    
    // Metadata
    private Long brokerId;
    private String brokerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Calculated fields
    private Integer age;
}