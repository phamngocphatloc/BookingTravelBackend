package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatBoxResponse {
        private UserInfoResponse UserChat;
        private List<MessageResponse> listMessage;
}
