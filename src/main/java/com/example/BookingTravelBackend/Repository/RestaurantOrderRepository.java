package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder,Integer> {
    @Query(value = "\n" +
            "SELECT CASE WHEN EXISTS " +
            "(SELECT 1 FROM restaurant_order ro JOIN restaurant_order_details rod ON ro.order_id = rod.order_id" +
            " JOIN menu_details md ON rod.product_id = md.menu_details_id  WHERE ro.user_id = ?1 AND md.menu_id = ?2)" +
            " THEN 1 ELSE 0 END", nativeQuery = true)
    int checkProductBuyed(int userId, int menuId);
}
