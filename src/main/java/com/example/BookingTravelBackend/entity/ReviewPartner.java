package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "ReviewPartner")
public class ReviewPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ReviewId")
    private int id;

    @Column (name = "Comment", columnDefinition = "nvarchar(255)")
    private String comment;

    @Column (name = "rate")
    private int rate;

    @ManyToOne
    @JoinColumn (name = "PartnerId")
    private HotelPartners partnerReview;

    @ManyToOne
    @JoinColumn (name = "UserId")
    private User userReview;
}
