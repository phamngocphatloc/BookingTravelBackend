package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.payload.respone.UserDetailsResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    int rate;

    private String review;

}
