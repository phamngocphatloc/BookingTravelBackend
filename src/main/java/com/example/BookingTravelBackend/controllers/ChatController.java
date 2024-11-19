package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ChatMessageDTO;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.MessageResponse;
import com.example.BookingTravelBackend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        // Log tin nhắn nhận được từ client
        System.out.println("Tin nhắn nhận được: " + chatMessageDTO);

        // Kiểm tra tính hợp lệ của dữ liệu
        if (chatMessageDTO.getReceiverId() == 0 || chatMessageDTO.getContent() == null) {
            return ResponseEntity.badRequest().body("Invalid message data");
        }

        // Lưu tin nhắn vào database
        MessageResponse response = messageService.saveMessage(chatMessageDTO);



        return ResponseEntity.ok(response);
    }


    // API REST lấy lịch sử tin nhắn
    @GetMapping("/history")
    public ResponseEntity<HttpRespone> getChatHistory(@RequestParam int receiverId) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", messageService.getChatHistory(receiverId)));
    }

    // API REST lấy lịch sử tin nhắn
    @GetMapping("/chat_box")
    public ResponseEntity<HttpRespone> getChatBox() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", messageService.getAllChatBoxByUserId(principal.getId())));
    }
}
