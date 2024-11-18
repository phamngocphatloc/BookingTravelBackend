package com.example.BookingTravelBackend.payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Data
public class BookingRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private int price;

    private int quantityBook;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date checkIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date checkOut;

    private int hotelId;
    private int typeRoom;
}
