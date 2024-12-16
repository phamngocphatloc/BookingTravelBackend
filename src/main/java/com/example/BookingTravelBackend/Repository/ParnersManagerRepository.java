package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.PartnersManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParnersManagerRepository extends JpaRepository<PartnersManager,Integer> {
    @Query (value = "select * from partners_manger where partners_id = ?1 and is_delete=0", nativeQuery = true)
    public List<PartnersManager> GetAllManagerByPartner (int partnerId);

    @Query (value = "select * from partners_manger where partners_id = ?1 and user_id = ?2 ", nativeQuery = true)
    public Optional<PartnersManager> findManagerByPartnerAndUser (int partnerId, int userId);
}
