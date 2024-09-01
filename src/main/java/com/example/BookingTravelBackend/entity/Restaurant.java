package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Restaurant")
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RestaurantId")
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "HotelId")
    Hotel hotelRestaurant;
    @Column (name = "RestaurantName", columnDefinition = "nvarchar(255)")
    private String restaurantName;
    @Column (name = "Active")
    private boolean active;
}
