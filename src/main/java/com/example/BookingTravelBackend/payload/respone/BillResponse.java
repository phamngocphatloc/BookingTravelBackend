package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Bill;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
public class BillResponse {
    private int id;


    private String firstName;


    private String lastName;

    private String phone;


    private int price;


    private Date createdAt;


    private BookingResponse booking;

    public BillResponse (Bill bill){
        this.id = bill.getId();
        this.firstName = bill.getFirstName();
        this.lastName = bill.getLastName();
        this.phone = bill.getPhone();
        this.price = bill.getPrice();
        this.createdAt = bill.getCreatedAt();
        this.booking = new BookingResponse(bill.getBooking());
    }
}
