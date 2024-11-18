package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingDetails,Integer> {
    @Query (value = "SELECT \n" +
            "    b.*\n" +
            "FROM \n" +
            "    booking_details b\n" +
            "JOIN\n" +
            "    booking bk ON b.booking_id = bk.booking_id\n" +
            "JOIN\n" +
            "    room r ON b.room_id = r.room_id\n" +
            "JOIN \n" +
            "    hotel h ON r.hotel_id = h.hotel_id\n" +
            "WHERE \n" +
            "    h.hotel_id = ?1 -- Tham số cho hotel_id\n" +
            "    AND (\n" +
            "        (?2 BETWEEN bk.check_in AND bk.check_out) -- Kiểm tra nếu ngày check-in nằm trong khoảng booking\n" +
            "        OR (?3 BETWEEN bk.check_in AND bk.check_out) -- Kiểm tra nếu ngày check-out nằm trong khoảng booking\n" +
            "        OR (?2 <= bk.check_in AND ?3 >= bk.check_out) -- Kiểm tra nếu khoảng thời gian bao phủ toàn bộ booking\n" +
            "    )\n" +
            "    AND (bk.status = 'active' OR bk.status = 'pending'); -- Trạng thái active hoặc pending\n", nativeQuery = true)
    public List<BookingDetails> listBookingByCheckinCheckout (int HotelId, Date checkin, Date Checkout);


}
