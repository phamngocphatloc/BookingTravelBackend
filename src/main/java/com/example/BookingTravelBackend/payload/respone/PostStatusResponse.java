package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostStatusResponse {
    private int totalLikes;
    private int totalDislikes;
    private int totalComments;

    public PostStatusResponse(int totalLikes, int totalDislikes, int totalComments) {
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.totalComments = totalComments;
    }
}
