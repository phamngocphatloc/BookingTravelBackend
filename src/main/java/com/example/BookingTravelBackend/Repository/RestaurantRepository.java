package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    @Query(value = "SELECT * \n" +
            "FROM restaurant \n" +
            "WHERE restaurant.hotel_id in (\n" +
            "    SELECT hotel.hotel_id \n" +
            "    FROM hotel \n" +
            "    JOIN room ON hotel.hotel_id = room.hotel_id \n" +
            "    JOIN booking_details ON room.room_id = booking_details.room_id \n" +
            "    JOIN booking ON booking.booking_id = booking_details.booking_id \n" +
            "    WHERE booking.booking_id = ?1\n" +
            ");", nativeQuery = true)
    Optional<Restaurant> findRestaurantByBillId(int billId);

    @Query (value = "SELECT CASE \n" +
            "            WHEN EXISTS (SELECT 1 FROM restaurant WHERE hotel_id = ?1) THEN 1 \n" +
            "            ELSE 0 \n" +
            "        END AS result;\n", nativeQuery = true)
    public int checkRestaurantByHotel (int hotelId);


}
