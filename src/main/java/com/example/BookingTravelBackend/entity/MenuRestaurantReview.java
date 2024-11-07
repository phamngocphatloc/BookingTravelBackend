package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table (name = "MenuRestaurantReview")
@Data
public class MenuRestaurantReview {
    @Id
    @Column(name = "ReviewId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "rate")
    int rate;

    @Column (name = "Review", columnDefinition = "nvarchar(500)")
    private String review;

    @ManyToOne
    @JoinColumn (name = "userId")
    private User userReview;
    @ManyToOne
    @JoinColumn (name = "MenuRestaurantId")
    private Menu menuReview;
    @OneToMany (mappedBy = "Review", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReviewFoodImg> listImg;
    @Column (columnDefinition = "nvarchar(1000)")
    private String reply;
}
