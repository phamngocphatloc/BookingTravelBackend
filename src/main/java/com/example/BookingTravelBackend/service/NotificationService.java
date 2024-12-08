package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.payload.respone.HttpRespone;

public interface NotificationService {
    public void addNotification (int userId, String title, String content, String link);
    public HttpRespone GetAllNotificationByPartner (int userId);
    public HttpRespone DeleteNotification (int notificationId);
}
