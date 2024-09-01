package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
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
    Bill bill;
    @OneToMany (mappedBy = "ItemOrder", fetch = FetchType.EAGER)
    Map<String,RestaurantOrderDetails> listItems = new HashMap<>();

    public int getTotalPrice (){
        AtomicInteger price = new AtomicInteger();
        listItems.values().stream().forEach(o -> {
            price.addAndGet(o.getProduct().getPrice() * o.getAmount());
        });
        return price.get();
    }
}
