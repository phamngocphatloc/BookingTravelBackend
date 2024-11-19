package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.MessageRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.Message;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ChatMessageDTO;
import com.example.BookingTravelBackend.payload.respone.ChatBoxResponse;
import com.example.BookingTravelBackend.payload.respone.MessageResponse;
import com.example.BookingTravelBackend.payload.respone.UserInfoResponse;
import com.example.BookingTravelBackend.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpls implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public MessageResponse saveMessage(ChatMessageDTO chatMessageDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Optional<User> senderOptional = userRepository.findById(principal.getId());
        Optional<User> receiverOptional = userRepository.findById(chatMessageDTO.getReceiverId());

        // Kiểm tra xem sender và receiver có tồn tại không
        if (senderOptional.isPresent() && receiverOptional.isPresent()) {
            User sender = senderOptional.get();
            User receiver = receiverOptional.get();

            // Tạo mới đối tượng Message
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(chatMessageDTO.getContent());
            message.setSentAt(new Timestamp(System.currentTimeMillis()));
            message.setRead(false);

            // Lưu tin nhắn vào database
            Message messagesaved = messageRepository.save(message);



                // Gửi tin nhắn tới người nhận qua WebSocket (dùng '/user/{email}/queue/messages')
                messagingTemplate.convertAndSendToUser(
                        receiver.getEmail(),           // Địa chỉ email người nhận
                        "/queue/messages",             // Dùng "/queue/messages" thay vì "/topic/messages"
                        new MessageResponse(messagesaved)                    // Gửi JSON
                );
            return new MessageResponse(messagesaved);
        } else {
            // Xử lý nếu không tìm thấy sender hoặc receiver
            throw new IllegalArgumentException("Không Tìm Thấy Người Gửi hoặc Người Nhận");
        }

    }



    @Override
    public ChatBoxResponse getChatHistory(int receiverId) {
        System.out.println("receoverId: "+receiverId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        System.out.println("SenderId: "+principal.getId());
        User userReceiver = userRepository.findById(receiverId).get();
        List<Message> listMessage = messageRepository.findChatHistory(principal.getId(),receiverId);
        ChatBoxResponse chatbox = new ChatBoxResponse();
        chatbox.setUserChat(new UserInfoResponse(userReceiver));
        List<MessageResponse> listMessageResponse = new ArrayList<>();
        listMessage.stream().forEach(item -> {
            listMessageResponse.add(new MessageResponse(item));
        });
        chatbox.setListMessage(listMessageResponse);
        return chatbox;
    }

    @Override
    public List<UserInfoResponse> getAllChatBoxByUserId(int userId) {
        List<UserInfoResponse> listResponse = new ArrayList<>();
        userRepository.selectAllUserChatByUserId(userId).stream().forEach(item -> {
            UserInfoResponse uresponse = new UserInfoResponse(item);
            listResponse.add(uresponse);
        });
        return listResponse;
    }
}
