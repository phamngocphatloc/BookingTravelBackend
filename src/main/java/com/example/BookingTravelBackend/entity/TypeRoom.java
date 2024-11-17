package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "TypeRooom")
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class TypeRoom {
    @Id
    @Column (name = "TypeRoomId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int typeRoomId;

    @Column (name = "TypeRoom", columnDefinition = "nvarchar(255)")
    private String typeRoom;
}
