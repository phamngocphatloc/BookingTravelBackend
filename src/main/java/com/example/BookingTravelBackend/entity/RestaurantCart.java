package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Data
@Getter
@Setter
@Table (name = "RestaurantCart")
public class RestaurantCart {
    @Id
    @Column(name = "CartId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    @ManyToOne
    @JoinColumn (name = "BillId")
    private Booking bill;
    @ManyToOne
    @JoinColumn (name = "UserId")
    private User UserCart;
    @OneToMany (mappedBy = "ItemCart",
            fetch = FetchType.EAGER,
            orphanRemoval = true, cascade = CascadeType.ALL)
    Map<String,CartDetails> listItems;
}
