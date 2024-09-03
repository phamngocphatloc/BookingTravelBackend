package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder,Integer> {
}
