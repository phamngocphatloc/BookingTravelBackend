package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query( value = "select * from transaction_wallet where transaction_type = ?1 or ?1 = '' ", nativeQuery = true)
    List<Transaction> findAllWithdrawal(String transaction_type);
}
