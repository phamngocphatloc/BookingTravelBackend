package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails,Integer> {
}
