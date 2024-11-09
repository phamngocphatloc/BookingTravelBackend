package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp startedAt; // Thời gian kết bạn

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;  // Người bạn thứ nhất

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;  // Người bạn thứ hai

    // Getters và Setters
}