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

    @Column (name = "PartnerName", columnDefinition = "nvarchar(255)")
    public String partnerName;

    @Column (name = "HotelName", columnDefinition = "nvarchar(255)")
    private String hotelName;

    @Column (name = "phone")
    private String phone;

    @Column (name = "email")
    private String email;

    @OneToMany (mappedBy = "requesttoCreatePartner", fetch = FetchType.EAGER)
    List<RequesttoCreateHotel> requestHotel;
    @ManyToOne
    @JoinColumn(name="UserId")
    private User userRequest;
    @Column (name ="BusinessLicense" )
    private String businessLicense;


}
