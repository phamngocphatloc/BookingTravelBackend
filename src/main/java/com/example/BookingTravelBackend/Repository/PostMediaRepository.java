package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia,Integer> {
}
