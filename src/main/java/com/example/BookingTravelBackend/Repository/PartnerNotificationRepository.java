package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.PartnerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerNotificationRepository extends JpaRepository<PartnerNotification, Integer> {
    @Query (value = "SELECT * \n" +
            "FROM partner_notification\n" +
            "WHERE CAST(created_date AS DATE) = CAST(GETDATE() AS DATE) \n" +
            "  AND is_delete = 0 \n" +
            "  AND partner_id = ?1\n" +
            "ORDER BY id DESC;\n", nativeQuery = true)
    public List<PartnerNotification> findAllNotificationByPartner (int partnerId);
}
