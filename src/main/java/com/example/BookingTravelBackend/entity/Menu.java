package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name = "MenuRestaurant")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MenuRestaurantId")
    private int id;

    @Column(name = "ProductName", columnDefinition = "nvarchar(255)")
    private String productName;

    @Column(name = "imgProduct", columnDefinition = "varchar(500)")
    private String imgProduct;

    @Column(name = "describe", columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name = "price")
    private int price;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menuReview")
    private List<MenuRestaurantReview> menuRestaurantReviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<MenuDetails> listItems;

    @ManyToOne
    @JoinColumn (name = "RestaurantId")
    private Restaurant restaurantSell;
}
