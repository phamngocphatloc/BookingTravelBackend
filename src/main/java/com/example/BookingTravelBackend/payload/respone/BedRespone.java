package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.TypeRoom;
import lombok.Getter;

@Getter
public class BedRespone {
    private int bedId;
    private String bedName;
    private int price;
    public BedRespone (TypeRoom type){
        this.bedId = type.getTypeRoomId();
        this.bedName = type.getTypeRoom();
        this.price = type.getPrice();
    }
}
