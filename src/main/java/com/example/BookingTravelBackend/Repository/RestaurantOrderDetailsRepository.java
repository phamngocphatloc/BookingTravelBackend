package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOrderDetailsRepository extends JpaRepository<RestaurantOrderDetails,Integer> {
}
