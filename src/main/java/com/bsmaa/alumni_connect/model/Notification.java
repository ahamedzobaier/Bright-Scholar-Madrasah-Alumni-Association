package com.bsmaa.alumni_connect.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientUsername; // The user who gets the alert
    private String message;
    private String link; // URL to redirect (e.g., "/blog/view/5")
    private boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
