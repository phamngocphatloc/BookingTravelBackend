package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RequesttoCreateHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateHotelRepository extends JpaRepository<RequesttoCreateHotel, Integer> {

}
