package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
}
