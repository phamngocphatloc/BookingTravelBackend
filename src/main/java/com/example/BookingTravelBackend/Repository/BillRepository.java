package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
    @Query (value = "SELECT bill.bill_id,bill.booking_id,bill.created_at,bill.first_name,bill.last_name,bill.price,bill.phone \n" +
            "FROM bill join booking on bill.booking_id = booking.booking_id \n" +
            "WHERE booking.status = 'Pending' and created_at < ?1",nativeQuery = true)
    List<Bill> findByStatusPendingAndCreatedAtBefore(Date time);

    @Query (value = "select bi.bill_id,bi.booking_id,bi.created_at,bi.first_name,bi.last_name,bi.phone,bi.price\n" +
            "from bill bi join booking b on bi.booking_id = b.booking_id join users u on b.user_id = u.user_id \n" +
            "where u.user_id = ?1", nativeQuery = true)
    public Page<Bill> findBookingByUser (int userId, Pageable page);

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


    @Query (value = "select b.bill_id,b.booking_id,b.created_at,b.first_name,b.last_name,b.price,b.phone \n" +
            "from bill b join booking \n" +
            "on b.booking_id = booking.booking_id join room \n" +
            "on booking.room_id = room.room_id join hotel \n" +
            "on room.hotel_id = hotel.hotel_id \n" +
            "where booking.status = ?1 and (hotel.hotel_id = ?2 or ?2 = 0)",nativeQuery = true)
    public List<Bill> selectBillByStatusAndHotel (String status, int hotelId);

}
