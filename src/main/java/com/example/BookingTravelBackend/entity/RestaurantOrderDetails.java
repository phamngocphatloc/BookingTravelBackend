package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "RestaurantOrderDetails")
@Data
public class RestaurantOrderDetails {
    @Id
    @Column(name = "OrderDetailsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "Quantity")
    private int amount;
    @Column (name = "Size")
    private String size;
    @ManyToOne
    @JoinColumn (name = "OrderId")
    private RestaurantOrder ItemOrder;
    @ManyToOne
    @JoinColumn (name = "ProductId")
    Menu product;
}
