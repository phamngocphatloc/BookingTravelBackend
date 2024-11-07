package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
public class ReviewFoodImg {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "ImgUrl", length = 1000)
    private String imgUrl;

    @ManyToOne
    @JoinColumn (name = "ReviewFoodId")
    private MenuRestaurantReview Review;


}
