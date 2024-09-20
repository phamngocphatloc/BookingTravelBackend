package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRestaurantReviewResponse {
    private int id;
    private String review;
    private UserDetailsResponse userReview;
    public MenuRestaurantReviewResponse (MenuRestaurantReview review){
        this.id = review.getId();
        this.review = review.getReview();
        this.userReview = new UserDetailsResponse(review.getUserReview());
    }
}
