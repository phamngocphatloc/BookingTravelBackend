package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.payload.Request.orderFoodDetailRequest;
import com.example.BookingTravelBackend.payload.respone.UserDetailsResponse;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import lombok.Getter;
import com.example.BookingTravelBackend.entity.RestaurantOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.BookingTravelBackend.payload.respone.OrderFoodDetailResponse;

@Getter
public class OrderFoodResponse {
    private int id;
    private Date orderDate;
    private String coupon;
    private String payment;
    private String Status;
    private UserDetailsResponse user;
    private BillResponse bill;
    private List<OrderFoodDetailResponse> listOrderDetails = new ArrayList<>();
    private int totalPrice = 0;
    private String room;
    public OrderFoodResponse (RestaurantOrder order){
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.coupon = order.getCoupon();
        this.payment = order.getPayment();
        this.Status = order.getStatus();
        this.user = new UserDetailsResponse(order.getUserOrder());
        this.bill = new BillResponse(order.getBookingBuyed());
        order.getListItems().forEach((key,value) -> {
            listOrderDetails.add(new OrderFoodDetailResponse(value));
        });
        this.totalPrice = order.getTotalPrice();
        this.room = order.getRoom();
    }
}
