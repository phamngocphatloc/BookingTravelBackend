package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Booking;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Data
public class
BillResponse {
    private int id;


    private String firstName;


    private String lastName;

    private String phone;


    private int price;


    private Date createdAt;

    UserInfoResponse userCreate;

    private List<BookingResponse> booking = new ArrayList<>();

    private Date checkIn;

    private Date checkOut;

    private String status;

    public BillResponse (Booking bill){
        this.id = bill.getId();
        this.firstName = bill.getFirstName();
        this.lastName = bill.getLastName();
        this.phone = bill.getPhone();
        this.price = bill.getPrice();
        this.createdAt = bill.getCreatedAt();
        bill.getListDetails().stream().forEach(item -> {
            this.booking.add(new BookingResponse(item));
        });
        this.userCreate = new UserInfoResponse(bill.getUserBooking());
        this.checkIn = bill.getCheckIn();
        this.checkOut = bill.getCheckOut();
        this.status = bill.getStatus();
    }
}
