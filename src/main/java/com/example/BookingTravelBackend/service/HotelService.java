package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;

import java.util.Date;

public interface HotelService {
    public PaginationResponse selectHotelByTour(TouristAttraction tour, int pageNum, int pageSize, Date checkIn, Date checkOut);
}
