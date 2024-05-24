package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {
    @Query(value = "select * from hotel join tourist_attraction on hotel.tourist_attaction_id = tourist_attraction.id where tourist_attraction.name like ?1",nativeQuery = true)
    public Page<Hotel> findByTouristAttraction (String tour, Pageable page);
}
