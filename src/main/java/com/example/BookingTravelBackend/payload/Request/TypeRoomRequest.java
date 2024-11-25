package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeRoomRequest {
    private int partnerId;
    private int hotelId;
    private String typeRoom;
    private int price;
}
