package com.example.BookingTravelBackend.payload.respone;


import com.example.BookingTravelBackend.entity.PartnerNotification;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PartnerNotificationResponse {
    private int id;
    private String title;
    private String content;
    private Date createdDate;
    private String link;
    private boolean isDelete;

    public PartnerNotificationResponse (PartnerNotification notification){
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.content = notification.getContent();
        this.createdDate = notification.getCreatedDate();
        this.link = notification.getLink();
        this.isDelete = notification.isDelete();
    }
}
