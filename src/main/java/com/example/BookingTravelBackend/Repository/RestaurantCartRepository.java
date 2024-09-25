package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantCartRepository extends JpaRepository<RestaurantCart,Integer> {
    @Query (value = "select * from restaurant_cart where user_id = ?1 and bill_id\n" +
            "= ?2", nativeQuery = true)
    public Optional<RestaurantCart> findByUserAndBill (int userId, int billId);
}
