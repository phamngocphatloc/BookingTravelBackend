package com.example.BookingTravelBackend.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class BillRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private int price;

    private BookingRequest booking;
}
