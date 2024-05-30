package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelServiceRepository extends JpaRepository<HotelService,Integer> {
    @Query (value = "select * from hotel_service where service_name like ?1",nativeQuery = true)
    public HotelService findByServiceName (String name);
}
