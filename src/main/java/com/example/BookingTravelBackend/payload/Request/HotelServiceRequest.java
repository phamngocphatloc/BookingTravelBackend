package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelServiceRequest {
    private String service;
    private int hotelId;
}
