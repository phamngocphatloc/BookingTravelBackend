package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table (name = "Room")
public class Room {
    @Id
    @Column (name = "RoomId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "NumberOfPeople")
    private int numberOfPeople;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable (joinColumns = @JoinColumn (name = "RoomId"), inverseJoinColumns = @JoinColumn (name = "BedId"))
    private List<Bed> Bed;
    @Column (name = "TypeRoom")
    private String typeRoom;
    @Column (name = "Describe", columnDefinition = "nvarchar(255)")
    private String describe;
    @Column (name = "price")
    private int price;
    @ManyToOne
    @JoinColumn (name = "HotelId")
    private Hotel hotelRoom;
    @Column (name = "roomName")
    private String roomName;
}
