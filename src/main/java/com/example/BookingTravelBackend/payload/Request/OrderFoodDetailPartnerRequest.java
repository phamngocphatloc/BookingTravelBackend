package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.RestaurantOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFoodDetailPartnerRequest {
    private int amount;
    private String size;
    private String name;
    private int productId;
    private int hotelId;
    private int partner;
}
