package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<TypeRoom,Integer> {
    @Query (value = "select * from bed where bed_name like ?1", nativeQuery = true)
    public TypeRoom findByBedNameLike (String name);
}
