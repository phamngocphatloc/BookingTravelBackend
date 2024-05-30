package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table (name = "Bill")
public class Bill {
    @Id
    @Column (name = "BillId")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "FirstName",columnDefinition = "nvarchar(255)")
    private String firstName;

    @Column (name = "LastName", columnDefinition = "nvarchar(255)")
    private String lastName;

    @Column (name = "phone")
    private String phone;

    @Column (name = "price")
    private int price;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "BookingId")
    private Booking booking;


}
