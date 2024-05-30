package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Bill;
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
}
