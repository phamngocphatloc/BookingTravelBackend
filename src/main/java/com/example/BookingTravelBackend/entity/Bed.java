package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "Bed")
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class Bed {
    @Id
    @Column (name = "BedId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int bedId;

    @Column (name = "BedName")
    private String bedName;
}
