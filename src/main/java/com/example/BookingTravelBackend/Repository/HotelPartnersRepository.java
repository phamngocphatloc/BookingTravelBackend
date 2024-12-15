package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.HotelPartners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelPartnersRepository extends JpaRepository<HotelPartners,Integer> {
    @Query (value = "select top 5 hotel_name from hotel_partners where partners_id in (select partners_id from hotel join tourist_attraction on hotel.tourist_attaction_id = tourist_attraction.id \n" +
            "where tourist_attraction.name like ?1) \n" +
            "and hotel_name like ?2",nativeQuery = true)
    public List<String> findHotelNameByTour (String tour, String hotelNameFind);
    public List<HotelPartners> findByWalletIsNull();
    @Query(value = "SELECT CASE " +
            "           WHEN EXISTS ( " +
            "               SELECT 1 " +
            "               FROM hotel_partners hp " +
            "               JOIN partners_manger pm ON hp.partners_id = pm.partners_id " +
            "               JOIN users u ON pm.user_id = u.user_id " +
            "               WHERE u.user_id = :userId AND hp.partners_id = :partnerId " +
            "           ) THEN 1 " +
            "           ELSE 0 " +
            "       END AS is_manager", nativeQuery = true)
    int isManagerOfPartner(@Param("userId") int userId, @Param("partnerId") int partnerId);


}
