package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query(value = "select * from report where status = 'pending'", nativeQuery = true)
    List<Report> findAllPending();
}
