package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.ReviewHotel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRespone {
    private int id;

    int rate;

    private String review;

    private UserDetailsResponse userReview;

    public ReviewRespone (ReviewHotel rv){
        this.id = rv.getId();
        this.review = rv.getReview();
        this.userReview = new UserDetailsResponse(rv.getUserReview());
    }
}
