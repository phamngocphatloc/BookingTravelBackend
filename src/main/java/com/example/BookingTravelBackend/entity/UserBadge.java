package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String badgeName;
    private String description;
    private Timestamp awardedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters và Setters
}