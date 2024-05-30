package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    @Query (value = "select * from room where room_name like ?1 and room.hotel_id = ?2", nativeQuery = true)
    public Optional<Room> findByRoomNameLikeAndHotel (String roomName, int hotelId);
}
