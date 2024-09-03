package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.MenuDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDetailsRepository extends JpaRepository<MenuDetails,Integer> {
}
