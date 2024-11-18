package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Booking,Integer> {
    @Query (value = "SELECT \n" +
            "    *\n" +
            "FROM \n" +
            "    booking\n" +
            "WHERE \n" +
            "    booking.status = 'Pending' \n" +
            "    AND booking.created_at < ?1",nativeQuery = true)
    List<Booking> findByStatusPendingAndCreatedAtBefore(Date time);

    @Query (value = "select * from booking where user_id = ?1", nativeQuery = true)
    public Page<Booking> findBookingByUser (int userId, Pageable page);

    @Query (value = "SELECT \n" +
            "    hotel.hotel_id,\n" +
            "    hotel.address,\n" +
            "    hotel.describe,\n" +
            "    COALESCE(SUM(bill.price), 0) AS total_price\n" +
            "FROM \n" +
            "    hotel\n" +
            "LEFT JOIN \n" +
            "    room ON hotel.hotel_id = room.hotel_id\n" +
            "LEFT JOIN \n" +
            "    booking ON room.room_id = booking.room_id\n" +
            "LEFT JOIN \n" +
            "    bill ON booking.booking_id = bill.booking_id\n" +
            "        AND bill.created_at BETWEEN ?2 AND ?3\n" +
            "WHERE \n" +
            "    (booking.status = 'active' OR booking.status = 'success') \n" +
            "    AND (hotel.hotel_id = ?1 OR ?1 = 0)\n" +
            "GROUP BY \n" +
            "    hotel.hotel_id, hotel.address, hotel.describe;", nativeQuery = true)
    public List<Object[]> selectRevenueByHotelAndDate (int hotelId,Date dateFrom, Date dateTo);

    @Query (value = "select room.room_name " +
            "from room " +
            "where room.room_id in (" +
            "select booking_details.room_id " +
            "from booking_details join booking " +
            "on booking_details.booking_id = booking.booking_id " +
            "where booking.booking_id = ?1" +
            ")",nativeQuery = true)
    public List<String> getAllRoomByBooking (int bookingId);



}
