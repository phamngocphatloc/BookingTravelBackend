package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String activityType; // Ví dụ: post, comment, like, v.v.
    private int contentId; // ID của bài viết hoặc bình luận
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
