package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table (name = "Booking_Details")
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class BookingDetails {
    @Id
    @Column (name = "DetailsId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int BookingId;

    @ManyToOne
    @JoinColumn(name = "BookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn (name = "RoomId")
    private Room roomBooking;



}
