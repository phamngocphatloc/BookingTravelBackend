package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.respone.TouristAttractionRespone;
import com.example.BookingTravelBackend.payload.respone.TouristAttractionTopRespone;

import java.util.List;

public interface TouristAttractionService {
    public List<TouristAttractionRespone> SelectAll ();
    public TouristAttraction selectByName (String name);
    List<TouristAttractionTopRespone> findTouristAttractionWithTotalRoomsBooked();
}
