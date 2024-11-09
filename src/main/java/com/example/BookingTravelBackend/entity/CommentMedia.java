package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class CommentMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mediaUrl;

    @ManyToOne
    @JoinColumn
            (name = "comment_id")
    private CommentPost comment;

    // Getters và Setters
}
