package com.mahi.marriagebrokerage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "interests")
@Data
@EqualsAndHashCode(exclude = {"fromClient", "toClient", "broker"})
@ToString(exclude = {"fromClient", "toClient", "broker"})
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_client_id", nullable = false)
    private Client fromClient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_client_id", nullable = false)
    private Client toClient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", nullable = false)
    private User broker;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    @Column(name = "message", length = 500)
    private String message;
    
    @Column(name = "response_message", length = 500)
    private String responseMessage;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        sentAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status != Status.PENDING && respondedAt == null) {
            respondedAt = LocalDateTime.now();
        }
    }
    
    public enum Status {
        PENDING, ACCEPTED, REJECTED, MATCHED
    }
}