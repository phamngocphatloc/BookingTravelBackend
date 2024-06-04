package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.ReviewHotel;
import com.example.BookingTravelBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewHotelRepository extends JpaRepository<ReviewHotel,Integer> {
    @Query (value = "select * from review_hotel where user_id = ?1",nativeQuery = true)
    public Optional<User> selectByUser (int userId);
}
