package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Booking;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
public class BookingResponse {
    private int BookingId;


    private UserDetailsResponse userBooking;

    private RoomRespone roomBooking;

    private Date checkIn;

    private Date checkOut;

    private String status;

    public BookingResponse (Booking booking){
        this.BookingId = booking.getBookingId();
        this.userBooking = new UserDetailsResponse(booking.getUserBooking());
        this.roomBooking = new RoomRespone(booking.getRoomBooking());
        this.checkIn = booking.getCheckIn();
        this.checkOut = booking.getCheckOut();
        this.status = booking.getStatus();
    }
}
