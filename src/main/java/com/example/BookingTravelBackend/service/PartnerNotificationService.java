package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.payload.respone.HttpRespone;

public interface PartnerNotificationService {
    public void addNotification (int PartnerId, String title, String content, String link);
    public HttpRespone GetAllNotificationByPartner (int partnerId);
    public HttpRespone DeleteNotification (int NotificationId);
}
