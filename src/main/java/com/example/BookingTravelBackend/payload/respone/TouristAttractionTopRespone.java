package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;

@Getter
public class TouristAttractionTopRespone {
    private String name;
    private String img;
    private int price;
    private int quantityBooking;

    public TouristAttractionTopRespone (Object[] objects){
        this.name = String.valueOf(objects[0]);
        this.img = String.valueOf(objects[1]);
        this.quantityBooking = (int) objects[2];
        this.price = (int) objects[3];
    }
}
