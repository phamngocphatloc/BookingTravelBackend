package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    @Query (value = "select * from invoice where invoice.id in " +
            "(select invoice.id from invoice " +
            "join booking on invoice.booking_id = booking.booking_id join booking_details on booking_details.booking_id = booking.booking_id " +
            "join room on booking_details.room_id = room.room_id\n" +
            "where room.hotel_id = ?1)", nativeQuery = true)
    public List<Invoice> findAllBookingByHotelId (int hotelId);
}
