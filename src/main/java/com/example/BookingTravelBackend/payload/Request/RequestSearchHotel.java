package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequestSearchHotel {
    private String search;
    private Date checkIn;
    private Date checkOut;
}
