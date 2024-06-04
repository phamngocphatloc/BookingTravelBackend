package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.ReviewHotel;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ReviewRequest;
import com.example.BookingTravelBackend.payload.respone.ReviewRespone;

import java.util.List;

public interface ReviewService {
    public List<ReviewRespone> selectAll ();
    public ReviewRespone Comment (ReviewRequest request, User user);
}
