package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {
    @Query(value = "select * from hotel join tourist_attraction on hotel.tourist_attaction_id = tourist_attraction.id where tourist_attraction.name like ?1",nativeQuery = true)
    public Page<Hotel> findByTouristAttraction (String tour, Pageable page);

    @Query (value = "select * from hotel_service where price = 0", nativeQuery = true)
    public List<Object[]> selectServiceHotelFree();

    @Query (value = "select * from hotel_service where price >= 100000 and price < 1000000", nativeQuery = true)
    public List<Object[]> selectServiceHotelLuxury();

    @Query (value = "select * from hotel_service where price >= 1000000", nativeQuery = true)
    public List<Object[]> selectServiceHotelVip();
}
