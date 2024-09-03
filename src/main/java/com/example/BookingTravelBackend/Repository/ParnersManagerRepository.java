package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.PartnersManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParnersManagerRepository extends JpaRepository<PartnersManager,Integer> {
}
