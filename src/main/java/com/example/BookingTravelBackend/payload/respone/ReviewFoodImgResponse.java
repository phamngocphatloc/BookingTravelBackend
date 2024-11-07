package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.ReviewFoodImg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFoodImgResponse {
    private int id;
    private String imgUrl;
    public ReviewFoodImgResponse (ReviewFoodImg reviewFoodImg){
        this.id = reviewFoodImg.getId();
        this.imgUrl = reviewFoodImg.getImgUrl();
    }
}
