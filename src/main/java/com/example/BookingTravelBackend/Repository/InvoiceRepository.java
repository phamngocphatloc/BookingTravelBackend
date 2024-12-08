package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Native query để tính doanh thu hàng tháng
    @Query(value =
            "WITH Months AS (\n" +
                    "    SELECT 1 AS month UNION ALL\n" +
                    "    SELECT 2 UNION ALL\n" +
                    "    SELECT 3 UNION ALL\n" +
                    "    SELECT 4 UNION ALL\n" +
                    "    SELECT 5 UNION ALL\n" +
                    "    SELECT 6 UNION ALL\n" +
                    "    SELECT 7 UNION ALL\n" +
                    "    SELECT 8 UNION ALL\n" +
                    "    SELECT 9 UNION ALL\n" +
                    "    SELECT 10 UNION ALL\n" +
                    "    SELECT 11 UNION ALL\n" +
                    "    SELECT 12\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    m.month,\n" +
                    "    COALESCE(SUM(DISTINCT i.amount), 0) AS totalRevenue -- Sử dụng DISTINCT để tránh tính trùng tiền\n" +
                    "FROM \n" +
                    "    Months m\n" +
                    "LEFT JOIN \n" +
                    "    Invoice i ON MONTH(i.create_at) = m.month \n" +
                    "    AND YEAR(i.create_at) = ?1 \n" +
                    "    AND i.status = 'PAID'\n" +
                    "LEFT JOIN \n" +
                    "    Booking b ON i.booking_id = b.booking_id\n" +
                    "LEFT JOIN \n" +
                    "    Booking_Details bd ON b.booking_id = bd.booking_id\n" +
                    "LEFT JOIN \n" +
                    "    Room r ON bd.room_id = r.room_id\n" +
                    "WHERE \n" +
                    "    r.hotel_id = ?2  -- Lọc theo hotel_id\n" +
                    "GROUP BY \n" +
                    "    m.month\n" +
                    "ORDER BY \n" +
                    "    m.month;",
            nativeQuery = true)
    List<Object[]> getTotalRevenueByMonth(int year, int hotelId);
}
