package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
    VerificationToken findByUserToken (User user);
    @Query(value = "select * from verification_token where user_id = ?1", nativeQuery = true)
    public Optional<VerificationToken> findByUserId(int userId);
    @Modifying
    @Query("DELETE FROM VerificationToken v WHERE v.userToken = ?1")
    void deleteByUserToken(User user);
}
