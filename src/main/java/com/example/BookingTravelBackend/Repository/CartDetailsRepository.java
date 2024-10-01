package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails,Integer> {
    @Modifying
    @Query(value = "DELETE FROM cart_details WHERE cart_id = ?1 AND list_items_key = ?2", nativeQuery = true)
    int deleteCartDetailsByCartIdAndKey(int cartId, String key);
}
