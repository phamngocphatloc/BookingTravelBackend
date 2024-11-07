package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.payload.Request.FoodReviewRequest;
import com.example.BookingTravelBackend.payload.Request.ReplyFoodReviewRequest;

public interface MenuService {
    Menu findById(int id);
    void review (FoodReviewRequest request);
    void reply (ReplyFoodReviewRequest request);
}
