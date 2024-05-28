package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "ReviewHotel")
@Getter
@Setter
@Data
public class ReviewHotel {
    @Id
    @Column (name = "ReviewId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "rate")
    int rate;

    @Column (name = "Review", columnDefinition = "nvarchar(500)")
    private String review;

    @ManyToOne
    @JoinColumn (name = "userId")
    private User userReview;
}
