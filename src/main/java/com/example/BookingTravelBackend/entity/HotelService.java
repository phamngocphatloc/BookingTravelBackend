package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Data
@Table (name = "HotelService")
public class HotelService {
    @Id
    @Column (name = "HotelServiceId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "ServiceName", columnDefinition = "nvarchar(255)")
    private String serviceName;

    @Column (name = "price")
    private int servicePrice;

}
