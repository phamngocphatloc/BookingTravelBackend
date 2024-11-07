package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.ReviewFoodImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewFoodImgRepository extends JpaRepository<ReviewFoodImg,Integer> {
}
