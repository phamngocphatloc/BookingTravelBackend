package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMeidaRequest {
    private String mediaUrl;
    private String mediaType; // image hoặc video
}
