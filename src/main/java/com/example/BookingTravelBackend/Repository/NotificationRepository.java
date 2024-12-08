package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    @Query(value = "SELECT * \n" +
            "FROM partner_notification\n" +
            "WHERE CAST(created_date AS DATE) = CAST(GETDATE() AS DATE) \n" +
            "  AND is_delete = 0 \n" +
            "  AND user_id = ?1\n" +
            "ORDER BY id DESC;\n", nativeQuery = true)
    public List<Notification> findAllNotificationByUser (int partnerId);
}
