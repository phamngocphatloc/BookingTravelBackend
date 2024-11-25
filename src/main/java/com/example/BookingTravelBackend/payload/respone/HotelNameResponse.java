package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelNameResponse {
    private int id;
    private String hotelName;
    private String address;
}
