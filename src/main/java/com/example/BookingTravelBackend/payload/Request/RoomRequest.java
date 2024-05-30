package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.Room;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
@Data
public class RoomRequest {
    @NotNull(message = "vui lòng nhập Số Người")
    private int numberOfPeople;
    @NotEmpty(message = "vui lòng nhập Giường")
    private String bed;
    @NotEmpty(message = "vui lòng nhập Loại Phòng")
    private String typeRoom;
    private String describe;
    @NotNull(message = "vui lòng nhập Giá")
    private int price;
    @NotNull(message = "vui lòng nhập Tên Phòng ")
    private String roomName;
    private int hotelId;
    public Room getRoom (Hotel hotel){
        Room room = new Room();
        room.setHotelRoom(hotel);
        room.setNumberOfPeople(this.numberOfPeople);
        room.setTypeRoom(this.typeRoom);
        room.setDescribe(this.describe);
        room.setRoomName(this.roomName);
        room.setPrice(this.price);
        room.setBed(new ArrayList<>());
        return room;
    }


}
