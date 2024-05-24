package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Bed;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
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
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
