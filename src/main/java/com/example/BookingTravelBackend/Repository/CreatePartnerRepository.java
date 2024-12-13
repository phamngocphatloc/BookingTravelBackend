package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.RequesttoCreatePartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatePartnerRepository extends JpaRepository<RequesttoCreatePartner, Integer> {

}
