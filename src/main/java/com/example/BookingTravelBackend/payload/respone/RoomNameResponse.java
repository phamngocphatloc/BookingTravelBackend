package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomNameResponse {
    private String roomName;

    public RoomNameResponse (String roomName){
        this.roomName = roomName;
    }
}
