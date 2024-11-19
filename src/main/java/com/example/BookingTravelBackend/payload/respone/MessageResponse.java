package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Message;
import com.example.BookingTravelBackend.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MessageResponse {
    private int id;
    private String content;
    private Timestamp sentAt;
    private UserInfoResponse sender;
    private UserInfoResponse receiver;
    private boolean isRead;

    public MessageResponse (Message message){
        this.id = message.getId();
        this.content = message.getContent();
        this.sender = new UserInfoResponse(message.getSender());
        this.receiver = new UserInfoResponse(message.getReceiver());
        this.isRead = message.isRead();
        this.sentAt = message.getSentAt();
    }
}
