package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class Hotel {
    @Id
    @Column (name = "HotelId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int HotelId;

    @Column (name = "Address",columnDefinition = "nvarchar(255)")
    private String address;

    @OneToMany (mappedBy = "hotelImage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<ImageDesbrice> images;

    @Column (name = "describe", columnDefinition = "nvarchar(255)")
    private String describe;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable (
    joinColumns = @JoinColumn(name = "hotel_id"),
    inverseJoinColumns = @JoinColumn(name = "HotelServiceId"))
    List<HotelService> listService;

    @ManyToOne
    @JoinColumn (name = "TouristAttactionId")
    private TouristAttraction TouristAttraction;

    @OneToMany (mappedBy = "hotelRoom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Room> listRooms;

    @ManyToOne
    @JoinColumn (name = "PartnersId")
    private HotelPartners Partner;

    @OneToOne (mappedBy = "hotelRestaurant", fetch = FetchType.EAGER)
    private Restaurant restaurant;

}
