package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RequesttoCreateHotel {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "AddRess")
    public String addRess;

    @Column (name = "Desbrice")
    public String desbrice;

    @OneToOne
    @JoinColumn (name = "TouristAttractionId")
    private TouristAttraction touristAttraction;

    @ManyToOne
    @JoinColumn (name = "RequestPartId", nullable = true)
    private RequesttoCreatePartner requesttoCreatePartner;

    @OneToOne
    @JoinColumn (name = "PartnerId", nullable = true)
    private HotelPartners partner;


}
