package com.mahi.marriagebrokerage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "clients")
@Data
@EqualsAndHashCode(exclude = {"broker", "sentInterests", "receivedInterests"})
@ToString(exclude = {"broker", "sentInterests", "receivedInterests"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Personal Information
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String email;
    
    private String occupation;
    private String education;
    private String religion;
    private String caste;
    private String subcaste;
    
    @Column(name = "annual_income")
    private Double annualIncome;
    
    private String city;
    private String state;
    private String country;
    
    @Column(name = "height_cm")
    private Integer heightCm;
    
    @Column(name = "weight_kg")
    private Integer weightKg;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;
    
    // Family Information
    @Column(name = "father_name")
    private String fatherName;
    
    @Column(name = "father_occupation")
    private String fatherOccupation;
    
    @Column(name = "mother_name")
    private String motherName;
    
    @Column(name = "mother_occupation")
    private String motherOccupation;
    
    @Column(name = "family_type")
    private String familyType; // Nuclear, Joint
    
    @Column(name = "family_income")
    private Double familyIncome;
    
    @Column(name = "siblings_count")
    private Integer siblingsCount;
    
    // Preferences
    @Column(name = "preferred_age_min")
    private Integer preferredAgeMin;
    
    @Column(name = "preferred_age_max")
    private Integer preferredAgeMax;
    
    @Column(name = "preferred_height_min")
    private Integer preferredHeightMin;
    
    @Column(name = "preferred_height_max")
    private Integer preferredHeightMax;
    
    @Column(name = "preferred_religion")
    private String preferredReligion;
    
    @Column(name = "preferred_caste")
    private String preferredCaste;
    
    @Column(name = "preferred_education")
    private String preferredEducation;
    
    @Column(name = "preferred_occupation")
    private String preferredOccupation;
    
    @Column(name = "preferred_income_min")
    private Double preferredIncomeMin;
    
    @Column(name = "preferred_location")
    private String preferredLocation;
    
    // Profile Information
    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;
    
    @Column(name = "horoscope_url")
    private String horoscopeUrl;
    
    @Column(name = "bio", length = 1000)
    private String bio;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    private User broker;
    
    @OneToMany(mappedBy = "fromClient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Interest> sentInterests;
    
    @OneToMany(mappedBy = "toClient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Interest> receivedInterests;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum MaritalStatus {
        NEVER_MARRIED, DIVORCED, WIDOWED, SEPARATED
    }
}