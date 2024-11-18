package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.BookingDetails;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
public class BookingResponse {
    private int BookingId;

    private RoomRespone roomBooking;


    public BookingResponse (BookingDetails booking){
        this.BookingId = booking.getBookingId();
        this.roomBooking = new RoomRespone(booking.getRoomBooking());
    }
}
