package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingUpdateRequest {
    private int bookingId;
    private String status;
}
