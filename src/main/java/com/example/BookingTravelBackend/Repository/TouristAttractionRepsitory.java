package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristAttractionRepsitory extends JpaRepository<TouristAttraction,Integer> {
    public TouristAttraction findByNameLike (String name);
}
