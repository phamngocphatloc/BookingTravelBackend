package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mediaUrl;
    private String mediaType; // image hoặc video

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}