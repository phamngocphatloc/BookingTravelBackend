package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.ImageDescribeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDesbriceRequestRepository extends JpaRepository<ImageDescribeRequest, Integer> {
}
