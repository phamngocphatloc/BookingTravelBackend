package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantCartRepository extends JpaRepository<RestaurantCart,Integer> {
}
