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
    @Query(value = "select * from hotel join tourist_attraction on hotel.tourist_attaction_id = tourist_attraction.id \n" +
            "where tourist_attraction.name like ?1 and (hotel_id in (select hotel_id from hotel join hotel_partners on hotel.partners_id = hotel_partners.partners_id\n" +
            "where hotel_name like ?2) or ?2 = '')",nativeQuery = true)
    public Page<Hotel> findByTouristAttraction (String tour, String hotelName, Pageable page);

    @Query (value = "select * from hotel_service where price = 0", nativeQuery = true)
    public List<Object[]> selectServiceHotelFree();

    @Query (value = "select * from hotel_service where price >= 100000 and price < 1000000", nativeQuery = true)
    public List<Object[]> selectServiceHotelLuxury();

    @Query (value = "select * from hotel_service where price >= 1000000", nativeQuery = true)
    public List<Object[]> selectServiceHotelVip();
}
