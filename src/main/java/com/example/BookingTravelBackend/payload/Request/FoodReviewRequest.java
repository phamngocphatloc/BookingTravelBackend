package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.ReviewFoodImg;
import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FoodReviewRequest {
    int rate;
    private int menuIdReview;
    private List<FoodReviewImgRequest> listImg;
    private String review;

    public MenuRestaurantReview getFoodReviewRequest (Menu menu){
        MenuRestaurantReview response = new MenuRestaurantReview();
        response.setReview(review);
        response.setMenuReview(menu);
        response.setRate(rate);
        return response;
    }
}
