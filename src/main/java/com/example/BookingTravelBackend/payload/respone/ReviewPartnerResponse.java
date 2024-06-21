package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.entity.ReviewPartner;
import com.example.BookingTravelBackend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPartnerResponse {
    private int id;
    private String comment;
    private int rate;
    private UserDetailsResponse userReview;

    public ReviewPartnerResponse (ReviewPartner review){
        this.id = review.getId();
        this.comment = review.getComment();
        this.rate = review.getRate();
        this.userReview = new UserDetailsResponse(review.getUserReview());
    }
}
