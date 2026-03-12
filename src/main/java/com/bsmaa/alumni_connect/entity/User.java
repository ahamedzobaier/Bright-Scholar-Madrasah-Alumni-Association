package com.bsmaa.alumni_connect.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // These were missing but exist in your MySQL table
    private String batch;
    private String department;

    @Enumerated(EnumType.STRING)
    private Role accountType;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // Matches your MySQL 'status' column

    private String profilePicUrl = "/images/default-avatar.png";

    private LocalDateTime createdAt = LocalDateTime.now();

    // Enums to match your MySQL ENUM types exactly
    public enum Role {
        GENERAL_MEMBER,
        COMMITTEE_MEMBER,
        ADVISOR,
        ADMIN
    }

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
