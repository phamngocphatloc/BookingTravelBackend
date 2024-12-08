package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.NotificationRepository;
import com.example.BookingTravelBackend.entity.Notification;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.NotificationResponse;
import com.example.BookingTravelBackend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpls implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository partnerNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void addNotification(int userId, String title, String content, String link) {
        Notification notification = new Notification();
        User user  = userRepository.findById(userId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Tìm Thấy User");
        });

        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setDelete(false);
        notification.setUser(user);
        partnerNotificationRepository.save(notification);
        // Gửi tin nhắn tới người nhận qua WebSocket (dùng '/user/{email}/queue/messages')
        messagingTemplate.convertAndSendToUser(
                String.valueOf(user.getEmail()),           // Địa chỉ email người nhận
                "/queue/partner",             // Dùng "/queue/messages" thay vì "/topic/messages"
                new NotificationResponse(notification)                    // Gửi JSON
        );
    }

    @Override
    public HttpRespone GetAllNotificationByPartner(int userId) {
        List<Notification> listPartner = partnerNotificationRepository.findAllNotificationByUser(userId);
        List<NotificationResponse> responses = new ArrayList<>();
        if (!listPartner.isEmpty()){
            listPartner.stream().forEach(item -> {
                responses.add(new NotificationResponse(item));
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", responses);
    }

    @Override
    @Transactional
    public HttpRespone DeleteNotification(int NotificationId) {
        Notification notification = partnerNotificationRepository.findById(NotificationId).orElseThrow(() ->{
            throw new IllegalArgumentException("Không Tìm Thấy Thông Báo");
        });
        notification.setDelete(true);
        Notification saved = partnerNotificationRepository.save(notification);

        return new HttpRespone(HttpStatus.OK.value(), "success", new NotificationResponse(saved));
    }
}
