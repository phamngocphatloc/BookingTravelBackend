package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "ImageDesbrice")
@Getter
@Setter
public class ImageDesbrice {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "Title")
    private String title;
    @Column (name = "link")
    private String link;

    @ManyToOne
    @JoinColumn (name = "HotelId")
    private Hotel hotelImage;
}
