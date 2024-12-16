package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.HotelPartners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnersRepository extends JpaRepository<HotelPartners, Integer> {
    @Query (value = "SELECT hotel_partners.* \n" +
            "FROM hotel_partners \n" +
            "JOIN partners_manger \n" +
            "    ON hotel_partners.partners_id = partners_manger.partners_id \n" +
            "WHERE partners_manger.user_id = ?1 \n" +
            "    AND partners_manger.is_delete = 0;\n", nativeQuery = true)
    public List<HotelPartners> ListAllHotelPartnersByUser (int userId);

    @Query (value = "SELECT \n" +
            "    CASE \n" +
            "        WHEN EXISTS (\n" +
            "            SELECT 1\n" +
            "            FROM hotel_partners \n" +
            "            JOIN partners_manger ON hotel_partners.partners_id = partners_manger.partners_id \n" +
            "            JOIN hotel ON hotel.partners_id = hotel_partners.partners_id\n" +
            "            WHERE hotel.hotel_id = ?2 \n" +
            "              AND partners_manger.user_id = ?1\n" +
            "        ) THEN 1\n" +
            "        ELSE 0\n" +
            "    END AS result;\n", nativeQuery = true)
    public int checkHotelPartnersByUser (int userId, int hotelId);
}
