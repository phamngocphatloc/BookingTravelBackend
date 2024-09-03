package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRestaurantReviewRepository extends JpaRepository<MenuRestaurantReview,Integer> {
}
