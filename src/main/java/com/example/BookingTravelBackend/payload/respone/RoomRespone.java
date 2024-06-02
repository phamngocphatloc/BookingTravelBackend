package com.example.BookingTravelBackend.payload.respone;


import com.example.BookingTravelBackend.entity.Room;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class RoomRespone {
    private int id;
    private int numberOfPeople;
    private List<BedRespone> bed = new ArrayList<>();
    private String typeRoom;
    private String describe;
    private int price;

    private String status;
    private String roomName;


    private int HotelId;

    private String address;



    public RoomRespone (Room room){
        this.id = room.getId();
        this.numberOfPeople = room.getNumberOfPeople();
        room.getBed().stream().forEach(item -> {
            bed.add(new BedRespone(item));
        });
        this.typeRoom = room.getTypeRoom();
        this.describe = room.getDescribe();
        this.price = room.getPrice();
        this.roomName = room.getRoomName();
        this.HotelId = room.getHotelRoom().getHotelId();
        this.address = room.getHotelRoom().getAddress();
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
