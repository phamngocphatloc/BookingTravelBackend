package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    @OneToMany (mappedBy = "restaurantSell", fetch = FetchType.LAZY)
    private List<Menu> listMenu;
    @Column (name = "restaurantImg", columnDefinition = "nvarchar(500)")
    private String restaurantImg;
    @Column (name = "authentic")
    private boolean authentic;
}
