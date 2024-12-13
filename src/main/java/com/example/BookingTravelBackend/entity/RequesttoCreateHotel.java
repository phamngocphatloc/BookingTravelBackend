package com.example.BookingTravelBackend.entity;

import com.example.BookingTravelBackend.payload.Request.ImageDesbriceRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class RequesttoCreateHotel {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "AddRess", columnDefinition = "nvarchar(255)")
    public String addRess;

    @Column (name = "Desbrice",columnDefinition = "nvarchar(255)")
    public String desbrice;

    @OneToOne
    @JoinColumn (name = "TouristAttractionId")
    private TouristAttraction touristAttraction;

    @ManyToOne
    @JoinColumn (name = "RequestPartId", nullable = true)
    private RequesttoCreatePartner requesttoCreatePartner;

    @OneToMany (fetch = FetchType.EAGER, mappedBy = "hotelRequest")
    private List<ImageDescribeRequest> imageDesbrice = new ArrayList<>();

    @OneToOne
    @JoinColumn (name = "PartnerId", nullable = true)
    private HotelPartners partner;

    private String status;
}
