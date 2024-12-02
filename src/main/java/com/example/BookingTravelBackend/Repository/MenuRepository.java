package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Integer> {
    @Query (value = "select * from menu_restaurant where restaurant_id = ?1", nativeQuery = true)
    Page<Menu> findByRestaurant(int restaurantId, Pageable pageable);
    @Query (value = "select * from menu_restaurant where restaurant_id = ?1", nativeQuery = true)
    List<Menu> findAllByRestaurant(int restaurantId);
    @Query(value = "SELECT CASE WHEN EXISTS (" +
            "  SELECT 1 " +
            "  FROM menu_restaurant_review " +
            "  WHERE user_id = ?1 AND menu_restaurant_id = ?2) " +
            "THEN 1 ELSE 0 END",
            nativeQuery = true)
    int checkRated(int userId, int menuId);
}
