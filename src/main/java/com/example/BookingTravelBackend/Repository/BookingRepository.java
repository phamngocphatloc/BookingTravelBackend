package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query (value = "SELECT * \n" +
            "FROM booking \n" +
            "WHERE booking.room_id IN (\n" +
            "    SELECT room.room_id \n" +
            "    FROM hotel \n" +
            "    JOIN room ON hotel.hotel_id = room.hotel_id \n" +
            "    WHERE hotel.hotel_id = ?1\n" +
            ") \n" +
            "AND ?2 BETWEEN booking.check_in AND booking.check_out\n" +
            "AND booking.check_out != ?2\n" +
            "AND status = 'active';", nativeQuery = true)
    public List<Booking> listBookingByCheckinCheckout (int HotelId, Date checkin, Date Checkout);
}
