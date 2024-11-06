package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.RestaurantOrderDetails;
import lombok.Getter;

@Getter
public class OrderFoodDetailResponse {
    private int id;
    private int amount;
    private String size;
    private MenuDetailsResponse ItemOrder;
    public OrderFoodDetailResponse (RestaurantOrderDetails order){
        this.id = order.getId();
        this.amount = order.getAmount();
        this.size = order.getSize();
        this.ItemOrder = new MenuDetailsResponse(order.getProduct());
    }
}
