package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
@Entity
@Data
@Getter
@Setter
@Table (name = "RestaurantOrder")
public class RestaurantOrder {
    @jakarta.persistence.Id
    @Column(name = "OrderId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column (name = "OrderDate")
    Date orderDate;
    @Column (name = "Coupon")
    private String coupon;
    @Column (name = "Payment")
    private String payment;
    @Column (name = "Status")
    private String Status;
    @ManyToOne
    @JoinColumn (name = "UserId")
    private User userOrder;
    @ManyToOne
    @JoinColumn (name = "BillId")
    Booking bookingBuyed;
    @OneToMany (mappedBy = "ItemOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Map<String,RestaurantOrderDetails> listItems = new HashMap<>();
    @Column (name = "roomOrder")
    private String room;
    @Column (name = "handler")
    private String handler;

    public int getTotalPrice() {
        AtomicInteger price = new AtomicInteger();
        listItems.forEach((key, o) -> {
            if (o != null && o.getProduct() != null) {
                int productPrice = o.getProduct().getPrice();
                int amount = o.getAmount();
                if (productPrice > 0 && amount > 0) {
                    price.addAndGet(productPrice * amount);
                }
            }
        });
        return price.get();
    }

}
