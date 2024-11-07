package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import com.example.BookingTravelBackend.entity.Restaurant;

@Getter
@Setter
public class MenuRestaurantReviewResponse {
    private int id;
    private String review;
    private UserDetailsResponse userReview;
    private int rate;
    private List<ReviewFoodImgResponse> listImg = new ArrayList<>();
    private String reply;
    private String restaurantName;
    private String restaurantImg;
    private boolean restaurantAuthentic;
    public MenuRestaurantReviewResponse (MenuRestaurantReview review){
        this.id = review.getId();
        this.review = review.getReview();
        this.userReview = new UserDetailsResponse(review.getUserReview());
        this.rate = review.getRate();
        review.getListImg().stream().forEach(item->{
            listImg.add(new ReviewFoodImgResponse(item));
        });
        this.reply = review.getReply();
        Restaurant restaurantSell = review.getMenuReview().getRestaurantSell();
        this.restaurantName = restaurantSell.getRestaurantName();
        this.restaurantImg = restaurantSell.getRestaurantImg();
        this.restaurantAuthentic = restaurantSell.isAuthentic();
    }
}
