package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query (value = "SELECT b.booking_id,b.check_in,b.check_out,b.room_id,b.status,b.user_id\n" +
            "FROM booking b\n" +
            "JOIN room r ON b.room_id = r.room_id\n" +
            "JOIN hotel h ON r.hotel_id = h.hotel_id\n" +
            "WHERE h.hotel_id = ?1\n" +
            "AND (\n" +
            "    (?2 BETWEEN b.check_in AND b.check_out)\n" +
            "    OR (?3 BETWEEN b.check_in AND b.check_out)\n" +
            "    OR (?2 <= b.check_in AND ?3 >= b.check_out)\n" +
            ")\n" +
            "AND (b.status = 'active' or b.status = 'pending');", nativeQuery = true)
    public List<Booking> listBookingByCheckinCheckout (int HotelId, Date checkin, Date Checkout);


}
