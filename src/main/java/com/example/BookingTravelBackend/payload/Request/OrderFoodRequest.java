package com.example.BookingTravelBackend.payload.Request;


import lombok.Getter;
import lombok.Setter;
import com.example.BookingTravelBackend.entity.RestaurantOrder;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class OrderFoodRequest {
   Date orderDate = new Date();
   private String coupon;
   private String payment;
   private int bill;
   private String room;
   public RestaurantOrder getOrder (){
    RestaurantOrder order = new RestaurantOrder();
    order.setStatus("pending");
    order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
    order.setPayment(payment);
    order.setCoupon(coupon);
    order.setRoom(room);
    return order;
    }
}
