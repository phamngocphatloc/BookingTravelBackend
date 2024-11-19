package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private Integer receiverId = 0;
    private String content;
}
