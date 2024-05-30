package com.example.BookingTravelBackend.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class HotelServiceRequest {
    private String service;
    private int hotelId;
}
