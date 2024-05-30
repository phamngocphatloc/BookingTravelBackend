package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.payload.Request.RoomRequest;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;

import java.util.Date;
import java.util.List;

public interface RoomService {
    public List<Room> selectRoomAllByHotel (Hotel hoel);
    public HotelRespone CheckRoomNotYet(HotelRespone response, Date checkIn, Date checkOut);

    public void addRoom (RoomRequest room);
}