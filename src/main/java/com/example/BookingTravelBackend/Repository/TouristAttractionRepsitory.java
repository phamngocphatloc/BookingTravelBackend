package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristAttractionRepsitory extends JpaRepository<TouristAttraction,Integer> {
    public TouristAttraction findByNameLike (String name);
    @Query(value = "SELECT \n" +
            "    top 3 t.name, \n" +
            "    t.img, \n" +
            "    COALESCE(count(b.room_id), 0) AS total_rooms_booked,\n" +
            "    COALESCE(MIN(r.price), 0) AS cheapest_room_price\n" +
            "FROM \n" +
            "    tourist_attraction t\n" +
            "LEFT JOIN \n" +
            "    hotel h ON t.id = h.tourist_attaction_id\n" +
            "LEFT JOIN \n" +
            "    room r ON h.hotel_id = r.hotel_id\n" +
            "LEFT JOIN \n" +
            "    booking b ON b.room_id = r.room_id\n" +
            "GROUP BY \n" +
            "    t.name, t.img\n" +
            "ORDER BY \n" +
            "    COALESCE(count(b.room_id), 0) DESC;", nativeQuery = true)
    List<Object[]> findTouristAttractionWithTotalRoomsBooked();
}
