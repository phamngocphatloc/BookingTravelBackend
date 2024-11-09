package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp sentAt;
    private boolean isAccepted;   // Trạng thái yêu cầu: đã chấp nhận (true) hay chưa (false)

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;  // Người gửi yêu cầu

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;  // Người nhận yêu cầu

}