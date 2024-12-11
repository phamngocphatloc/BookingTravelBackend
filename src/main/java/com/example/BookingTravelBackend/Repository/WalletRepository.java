package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.entity.User;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    @Query (value = "select * from wallet where partner_id = ?1", nativeQuery = true)
    public Optional<Wallet> findWalletById (int partnerId);

    Optional<Wallet> findByPartner(HotelPartners partner);

    Optional<Wallet> findByUser(User user);


}
