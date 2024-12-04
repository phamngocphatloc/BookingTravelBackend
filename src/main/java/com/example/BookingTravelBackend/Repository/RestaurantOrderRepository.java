package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder,Integer> {
    @Query(value = "\n" +
            "SELECT CASE WHEN EXISTS " +
            "(SELECT 1 FROM restaurant_order ro JOIN restaurant_order_details rod ON ro.order_id = rod.order_id" +
            " JOIN menu_details md ON rod.product_id = md.menu_details_id  WHERE ro.user_id = ?1 AND md.menu_id = ?2)" +
            " THEN 1 ELSE 0 END", nativeQuery = true)
    int checkProductBuyed(int userId, int menuId);
    @Query (value = "SELECT * \n" +
            "FROM restaurant_order \n" +
            "WHERE bill_id IN (\n" +
            "    SELECT booking.booking_id \n" +
            "    FROM booking \n" +
            "    JOIN booking_details \n" +
            "        ON booking_details.booking_id = booking.booking_id \n" +
            "    JOIN room \n" +
            "        ON booking_details.room_id = room.room_id \n" +
            "    JOIN hotel \n" +
            "        ON room.hotel_id = hotel.hotel_id \n" +
            "    WHERE hotel.hotel_id = ?1 \n" +
            ") order by restaurant_order.order_id desc;", nativeQuery = true)
    public List<RestaurantOrder> findAllOrderByHotelId (int hotelId);

    @Query (value = "SELECT \n" +
            "    * \n" +
            "FROM \n" +
            "    restaurant_order \n" +
            "WHERE \n" +
            "    restaurant_order.status = 'Pending' \n" +
            "    AND restaurant_order.order_date < ?1", nativeQuery = true)
    public List<RestaurantOrder> GetAllRestaurantOrderOrderdateBefore (Date time);
}
