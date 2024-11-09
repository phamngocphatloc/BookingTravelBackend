package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
//báo cáo nội dung
public class ContentFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int contentId; // ID của bài viết hoặc bình luận
    private String reason;
    private String status;
    private Timestamp flaggedAt;

    @ManyToOne
    @JoinColumn(name = "flagged_by")
    private User flaggedBy;

}