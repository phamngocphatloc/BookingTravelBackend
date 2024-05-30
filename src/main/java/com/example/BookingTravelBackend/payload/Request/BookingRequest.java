package com.example.BookingTravelBackend.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class BookingRequest {

    private int userBookingId;

    private int roomBookingId;

    private Date checkIn;

    private Date checkOut;

    private String status;
}
