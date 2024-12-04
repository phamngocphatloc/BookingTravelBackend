package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.PartnerNotificationRepository;
import com.example.BookingTravelBackend.entity.PartnerNotification;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.MessageResponse;
import com.example.BookingTravelBackend.payload.respone.PartnerNotificationResponse;
import com.example.BookingTravelBackend.service.PartnerNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.Repository.HotelPartnersRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerNotificationServiceImpls implements PartnerNotificationService {
    private final HotelPartnersRepository hotelPartnersRepository;
    private final PartnerNotificationRepository partnerNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void addNotification(int PartnerId, String title, String content, String link) {
        PartnerNotification notification = new PartnerNotification();
        HotelPartners partner = hotelPartnersRepository.findById(PartnerId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Tìm Thấy Partner");
        });

        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setDelete(false);
        notification.setHotelPartners(partner);
        partnerNotificationRepository.save(notification);
        // Gửi tin nhắn tới người nhận qua WebSocket (dùng '/user/{email}/queue/messages')
        messagingTemplate.convertAndSendToUser(
                String.valueOf(PartnerId),           // Địa chỉ email người nhận
                "/queue/partner",             // Dùng "/queue/messages" thay vì "/topic/messages"
                new PartnerNotificationResponse(notification)                    // Gửi JSON
        );
    }

    @Override
    public HttpRespone GetAllNotificationByPartner(int partnerId) {
        List<PartnerNotification> listPartner = partnerNotificationRepository.findAllNotificationByPartner(partnerId);
        List<PartnerNotificationResponse> responses = new ArrayList<>();
        if (!listPartner.isEmpty()){
            listPartner.stream().forEach(item -> {
                responses.add(new PartnerNotificationResponse(item));
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", responses);
    }

    @Override
    @Transactional
    public HttpRespone DeleteNotification(int NotificationId) {
        PartnerNotification notification = partnerNotificationRepository.findById(NotificationId).orElseThrow(() ->{
            throw new IllegalArgumentException("Không Tìm Thấy Thông Báo");
        });
        notification.setDelete(true);
        PartnerNotification saved = partnerNotificationRepository.save(notification);

        return new HttpRespone(HttpStatus.OK.value(), "success", new PartnerNotificationResponse(saved));
    }
}
