package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.payload.Request.TypeRoomRequest;
import com.example.BookingTravelBackend.payload.respone.HotelPartnersResponse;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.TypeRoomResponse;

import java.util.List;

public interface PartnersHotelService {
    List<HotelPartnersResponse> listPartnersByUserId ();
    public boolean checkHotelPartnersByHotelId (int hotelId);
    public List<TypeRoomResponse> selectAllTypeRoomByPartnersId (int partNerId);
    public TypeRoomResponse saveTypeRoom (TypeRoomRequest request);
    public HttpRespone GetAllPartners();
    public HttpRespone RoomReservationNumber (int hotelId);
}
