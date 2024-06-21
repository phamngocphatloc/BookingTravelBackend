package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "PartnersManger")
public class PartnersManager {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "PartnerManagerId")
    private int id;

    @ManyToOne
    @JoinColumn (name = "UserId")
    private User userManager;

    @ManyToOne
    @JoinColumn (name = "PartnersId")
    private HotelPartners hotelPartners;

    @Column (name = "Position")
    private String Position;
}
