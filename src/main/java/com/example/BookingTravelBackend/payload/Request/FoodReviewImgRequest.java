package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import com.example.BookingTravelBackend.entity.ReviewFoodImg;
import lombok.Setter;

@Getter
@Setter
public class FoodReviewImgRequest {
    private String imgUrl;

    public ReviewFoodImg getReviewFoodImg (){
        ReviewFoodImg response = new ReviewFoodImg();
        response.setImgUrl(imgUrl);
        return response;
    }
}
