package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table (name = "Booking")
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class Booking {
    @Id
    @Column (name = "BookingId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int BookingId;

    @ManyToOne
    @JoinColumn (name = "UserId")
    private User userBooking;

    @ManyToOne
    @JoinColumn (name = "RoomId")
    private Room roomBooking;

    @Column (name = "CheckIn")
    private Date checkIn;

    @Column (name = "CheckOut")
    private Date checkOut;

    @Column (name = "Status")
    private String status;
}
