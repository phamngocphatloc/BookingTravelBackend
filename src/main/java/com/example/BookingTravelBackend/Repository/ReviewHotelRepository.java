package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.ReviewHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewHotelRepository extends JpaRepository<ReviewHotel,Integer> {
}
