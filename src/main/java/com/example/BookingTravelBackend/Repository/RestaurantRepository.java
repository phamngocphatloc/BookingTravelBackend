package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    @Query(value = "SELECT * FROM restaurant WHERE restaurant.hotel_id = (" +
            "SELECT hotel.hotel_id FROM hotel " +
            "JOIN room ON hotel.hotel_id = room.hotel_id " +
            "JOIN booking ON room.room_id = booking.room_id " +
            "JOIN bill ON bill.booking_id = booking.booking_id " +
            "WHERE bill.bill_id = ?1)", nativeQuery = true)
    Optional<Restaurant> findRestaurantByBillId(int billId);

}
