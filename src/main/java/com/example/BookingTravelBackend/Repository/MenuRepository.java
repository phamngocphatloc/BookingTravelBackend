package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Integer> {
    @Query (value = "select * from menu_restaurant where restaurant_id = ?1", nativeQuery = true)
    Page<Menu> findByRestaurant(int restaurantId, Pageable pageable);
}
