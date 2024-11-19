package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Message;
import com.example.BookingTravelBackend.payload.Request.ChatMessageDTO;
import com.example.BookingTravelBackend.payload.respone.ChatBoxResponse;
import com.example.BookingTravelBackend.payload.respone.MessageResponse;
import com.example.BookingTravelBackend.payload.respone.UserInfoResponse;

import java.util.List;

public interface MessageService {
    public MessageResponse saveMessage(ChatMessageDTO chatMessageDTO);
    public ChatBoxResponse getChatHistory(int receiverId);
    public List<UserInfoResponse> getAllChatBoxByUserId(int userId);
}
