package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MenuRestaurantRequest {
    private String productName;

    private String imgProduct;

    private String description;

    private int price;

    private int restaurantSellId;

    private int hotelId;
}
