package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "CartDetails")
@Data
public class CartDetails {
    @Id
    @Column(name = "CartDetailsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "Quantity")
    private int amount;
    @Column (name = "Size")
    private String size;
    @ManyToOne
    @JoinColumn (name = "CartId")
    private RestaurantCart ItemCart;
    @ManyToOne
    @JoinColumn (name = "ProductId")
    MenuDetails product;
}
