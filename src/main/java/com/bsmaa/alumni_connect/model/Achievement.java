package com.bsmaa.alumni_connect.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "achievements")
@Data
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String winnerName; // Name of the alumni/student

    @Column(columnDefinition = "TEXT")
    private String details;

    private String category; // e.g., Research, Sports, Corporate, Entrepreneurship

    private String imageUrl; // For the award/medal photo

    private String year; // e.g., "2024" or "Class of 2018"

    @Column(name = "recognition_date")
    private LocalDate recognitionDate;
}
