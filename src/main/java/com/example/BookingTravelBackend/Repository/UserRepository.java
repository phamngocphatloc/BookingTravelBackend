package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT u FROM User u WHERE u.phone = ?1")
    Optional<User> findByPhone(String email);
    @Query (value = "SELECT * \n" +
            "FROM users \n" +
            "WHERE user_id = (\n" +
            "    SELECT booking.user_id \n" +
            "    FROM booking \n" +
            "    WHERE booking.booking_id = ?1\n" +
            ");",nativeQuery = true)
    Optional<User> findUserByBillId (int billId);
}