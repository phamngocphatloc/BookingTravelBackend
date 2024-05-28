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
@RequiredArgsConstructor
@Data
@Table (name = "TouristAttraction")
public class TouristAttraction {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "Name", columnDefinition = "nvarchar(255)")
    private String name;

    @Column (name = "img")
    private String img;

    @OneToMany (mappedBy = "TouristAttraction")
    private List<Hotel> hotels;
}
