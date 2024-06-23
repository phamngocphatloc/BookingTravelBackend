package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.Request.HotelRequest;
import com.example.BookingTravelBackend.payload.Request.HotelRequestEdit;
import com.example.BookingTravelBackend.payload.Request.HotelServiceRequest;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.HotelServiceRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;

import java.util.Date;
import java.util.List;

public interface HotelService {
    public PaginationResponse selectHotelByTour(TouristAttraction tour, int pageNum, int pageSize,String hotelName, Date checkIn, Date checkOut);
    public List<HotelServiceRespone> selectServiceHotelFree();
    public List<HotelServiceRespone> selectServiceHotelLuxry();
    public List<HotelServiceRespone> selectServiceHotelVip();
    public void addHotel (HotelRequest hotelRequest);
    public HotelRespone selectById(int id, Date checkIn, Date checkOut);
    public void addHotelService (HotelServiceRequest request);
    public PaginationResponse selectHotelByCheckInCheckOut (int pageNum, int pageSize, Date checkIn, Date checkOut);
    public HotelRespone editHotel (HotelRequestEdit request);
    public void deleteHotel (int hotelId);
    public List<HotelRespone> listHotel ();
    public  HotelRespone selectHotelId (int hotelId);
    public List<String> findHotelNameByTour(String tour, String hotelNaneFind);
}
