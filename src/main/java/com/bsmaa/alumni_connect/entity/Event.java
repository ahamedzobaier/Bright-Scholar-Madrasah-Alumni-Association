package com.bsmaa.alumni_connect.entity; // This matches your folder structure

import java.time.LocalDateTime; // Fixes 'Entity', 'Table', 'Id', 'GeneratedValue'

import jakarta.persistence.Column;          // Fixes 'Data'
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "events")
@Data // This automatically generates Getters and Setters
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime eventDate;

    private String location;

    private String imageUrl;
}
