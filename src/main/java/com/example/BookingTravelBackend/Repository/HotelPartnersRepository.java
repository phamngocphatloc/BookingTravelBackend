package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.HotelPartners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelPartnersRepository extends JpaRepository<HotelPartners,Integer> {
    @Query (value = "select top 5 hotel_name from hotel_partners where partners_id in (select partners_id from hotel join tourist_attraction on hotel.tourist_attaction_id = tourist_attraction.id where tourist_attraction.name like N'Thành Phố Hồ Chí Minh') and hotel_name like ?2)",nativeQuery = true)
    public List<String> findHotelNameByTour (String tour, String hotelNameFind);
}
