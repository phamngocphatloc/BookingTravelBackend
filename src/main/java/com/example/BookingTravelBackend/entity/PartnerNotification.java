package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class PartnerNotification {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "Title", columnDefinition = "nvarchar(255)")
    private String title;

    @Column (name = "content", columnDefinition = "nvarchar(255)")
    private String content;

    @Column(nullable = false)
    private Date createdDate = new Date(); // Thời gian tạo thông báo

    @Column (name = "link")
    private String link;

    @ManyToOne
    @JoinColumn (name = "partnerId")
    private HotelPartners hotelPartners;

    @Column (name = "IsDelete")
    private boolean isDelete;
}
