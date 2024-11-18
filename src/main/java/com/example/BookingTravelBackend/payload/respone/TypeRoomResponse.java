package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.TypeRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeRoomResponse {

    private int typeRoomId;

    private String typeRoom;

    private int price;

    private int quantityRoomStill;

    public TypeRoomResponse (TypeRoom typeRoom){
        this.typeRoomId = typeRoom.getTypeRoomId();
        this.typeRoom = typeRoom.getTypeRoom();
        this.price = typeRoom.getPrice();
    }
}
