package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reportType; // Ví dụ: "content", "user", "spam"
    private String description;
    private String status; // Ví dụ: "pending", "resolved", "dismissed"
    private Timestamp reportedAt;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser; // Đối tượng bị báo cáo

}