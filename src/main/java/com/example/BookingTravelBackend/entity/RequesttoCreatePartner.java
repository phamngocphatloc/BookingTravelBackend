package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class RequesttoCreatePartner {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int requestId;

    @Column (name = "PartnerName")
    public String partnerName;

    @Column (name = "HotelName")
    private String hotelName;

    @Column (name = "phone")
    private String phone;

    @Column (name = "email")
    private String email;

    @OneToMany (mappedBy = "requesttoCreatePartner")
    List<RequesttoCreateHotel> requestHotel;



}
