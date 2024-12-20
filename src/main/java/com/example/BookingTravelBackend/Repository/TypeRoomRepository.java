package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRoomRepository extends JpaRepository<TypeRoom,Integer> {
    @Query (value = "select * from type_rooom where type_room like ?1", nativeQuery = true)
    public TypeRoom findTypeRoomNameLike (String name);

    @Query (value = "select * from type_rooom where type_room_id in (select type_room_id from room_type_room_list join room on room_type_room_list.room_id = room.room_id join hotel on room.hotel_id = hotel.hotel_id where hotel.hotel_id = ?1)",nativeQuery = true)
    public List<TypeRoom> findTypeRoomByHotel (int hotelId);
    @Query (value = "select * from type_rooom where type_rooom.partners_id = ?1",nativeQuery = true)
    public List<TypeRoom> findTypeRoomByPartNers (int hotelId);
    @Query(value = "select * from type_rooom where partners_id = ?1 and type_room like concat('%', ?2, '%')", nativeQuery = true)
    public Optional<TypeRoom> selectTypeRoomByPartnerAndNameAndPrice(int partnerId, String name, int price);

}
