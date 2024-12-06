package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table (name = "Booking")
public class Booking {
    @Id
    @Column(name = "BookingId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FirstName", columnDefinition = "nvarchar(255)")
    private String firstName;

    @Column(name = "LastName", columnDefinition = "nvarchar(255)")
    private String lastName;

    @ManyToOne
    @JoinColumn (name = "UserId")
    private User userBooking;

    @Column(name = "phone")
    private String phone;

    @Column(name = "price")
    private int price;

    @Column (name = "CheckIn")
    private Date checkIn;

    @Column (name = "CheckOut")
    private Date checkOut;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany (mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BookingDetails> listDetails;

    @OneToMany (mappedBy = "booking")
    private List<Invoice> invoice;

    @OneToMany (mappedBy = "bookingBuyed", fetch = FetchType.EAGER)
    private List<RestaurantOrder> listOrderFood;
    private String status;
}
