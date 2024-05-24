package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Getter
public class HotelRespone {


    private int HotelId;

    private String address;

    List<ImageDesbriceRespone> images = new ArrayList<>();

    private String describe;
    List<HotelServiceRespone> listService = new ArrayList<>();

    private List<RoomRespone> listRooms = new ArrayList<>();

    private String status;


    public HotelRespone (Hotel hotel){
        this.HotelId = hotel.getHotelId();
        this.address = hotel.getAddress();
        hotel.getImages().stream().forEach(item -> {
            images.add(new ImageDesbriceRespone(item));
        });
        this.describe = hotel.getDescribe();
        hotel.getListService().stream().forEach(item -> {
            listService.add(new HotelServiceRespone(item));
        });
        hotel.getListRooms().stream().forEach(item -> {
            listRooms.add(new RoomRespone(item));
        });
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
