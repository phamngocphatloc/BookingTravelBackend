package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.MenuDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDetailsRepository extends JpaRepository<MenuDetails,Integer> {
    @Query (value = "select * from menu_details where menu_id = ?1 and size = ?2", nativeQuery = true)
    MenuDetails findMenuDetailsByMenuIdAndSize (int menuId, String size);
}
