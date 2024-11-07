package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyFoodReviewRequest {
    private int reviewId;
    private String repLy;
}
