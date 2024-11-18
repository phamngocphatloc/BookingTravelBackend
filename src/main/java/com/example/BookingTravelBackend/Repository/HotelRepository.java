package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query (value = "SELECT CASE \n" +
            "           WHEN EXISTS (\n" +
            "               SELECT 1 \n" +
            "               FROM hotel \n" +
            "               JOIN hotel_partners ON hotel.partners_id = hotel_partners.partners_id \n" +
            "               JOIN partners_manger ON hotel_partners.partners_id = partners_manger.partners_id \n" +
            "               JOIN restaurant ON restaurant.hotel_id = hotel.hotel_id \n" +
            "               WHERE hotel.hotel_id = ?1 AND user_id = ?2\n" +
            "           ) THEN 1 \n" +
            "           ELSE 0 \n" +
            "       END;\n",nativeQuery = true)
    public int isAdminHotel (int hotelId, int userId);

    @Query (value = "SELECT * \n" +
            "FROM hotel \n" +
            "WHERE hotel.hotel_id in (\n" +
            "    SELECT hotel.hotel_id \n" +
            "    FROM hotel \n" +
            "    JOIN room ON hotel.hotel_id = room.hotel_id \n" +
            "    JOIN booking_details ON booking_details.room_id = room.room_id \n" +
            "    JOIN booking ON booking.booking_id = booking_details.booking_id \n" +
            "    WHERE booking.booking_id = ?1\n" +
            ");",nativeQuery = true)
    public Optional<Hotel> findHotelByBillId (int billId);
}
