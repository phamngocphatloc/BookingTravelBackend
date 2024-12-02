package com.example.BookingTravelBackend.payload.Request;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RestaurantRequest {
    int hotelRestaurantId;
    private String restaurantName;
    private String restaurantImg;
}
