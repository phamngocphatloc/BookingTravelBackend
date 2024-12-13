package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.ImageDesbrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDesbriceRepository extends JpaRepository<ImageDesbrice, Integer> {
}
