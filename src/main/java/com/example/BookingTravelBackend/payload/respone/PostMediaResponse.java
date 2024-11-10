package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMediaResponse {
    private String mediaUrl;
    private String mediaType; // image hoặc video
}
