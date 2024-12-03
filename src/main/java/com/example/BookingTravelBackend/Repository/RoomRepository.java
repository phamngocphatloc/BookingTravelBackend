package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "select * from room where room_name like ?1 and room.hotel_id = ?2", nativeQuery = true)
    public Optional<Room> findByRoomNameLikeAndHotel(String roomName, int hotelId);

    @Query(value = "SELECT \n" +
            "    r.*\n" +
            "FROM \n" +
            "    room r\n" +
            "JOIN \n" +
            "    room_type_room_list rtrl ON r.room_id = rtrl.room_id -- Liên kết với bảng trung gian\n" +
            "JOIN \n" +
            "    type_rooom tr ON rtrl.type_room_id = tr.type_room_id -- Liên kết với bảng loại phòng\n" +
            "WHERE \n" +
            "    r.room_id NOT IN (\n" +
            "        SELECT \n" +
            "            bd.room_id\n" +
            "        FROM \n" +
            "            booking b\n" +
            "        JOIN \n" +
            "            booking_details bd ON bd.booking_id = b.booking_id\n" +
            "        WHERE \n" +
            "            (\n" +
            "               (?2 BETWEEN b.check_in AND b.check_out)\n" +
            "                OR (?3 BETWEEN b.check_in AND b.check_out)\n" +
            "                OR (?2 <= b.check_in AND ?3 >= b.check_out)\n" +
            "            )\n" +
            "            AND b.status IN ('active', 'pending')\n" +
            "    ) \n" +
            "    AND r.hotel_id = ?1 \n" +
            "    AND tr.type_room_id = ?4\n" +
            "    AND r.is_delete = 0", nativeQuery = true)
    public List<Room> selectAllRoomByCheckInCheckOutAndType(int hotelId, Date checkin, Date checkout, int typeRoomId);


    @Query(value = "SELECT \n" +
            "     COUNT(*)\n" +
            "FROM \n" +
            "    room r\n" +
            "JOIN \n" +
            "    room_type_room_list rtrl ON r.room_id = rtrl.room_id -- Liên kết với bảng trung gian\n" +
            "JOIN \n" +
            "    type_rooom tr ON rtrl.type_room_id = tr.type_room_id -- Liên kết với bảng loại phòng\n" +
            "WHERE \n" +
            "    r.room_id NOT IN (\n" +
            "        SELECT \n" +
            "            bd.room_id\n" +
            "        FROM \n" +
            "            booking b\n" +
            "        JOIN \n" +
            "            booking_details bd ON bd.booking_id = b.booking_id\n" +
            "        WHERE \n" +
            "            (\n" +
            "               (?2 BETWEEN b.check_in AND b.check_out)\n" +
            "                OR (?3 BETWEEN b.check_in AND b.check_out)\n" +
            "                OR (?2 <= b.check_in AND ?3 >= b.check_out)\n" +
            "            )\n" +
            "            AND b.status IN ('active', 'pending')\n" +
            "    ) \n" +
            "    AND r.hotel_id = ?1 \n" +
            "    AND tr.type_room_id = ?4\n" +
            "    AND r.is_delete = 0", nativeQuery = true)
    public int selectCountRoomByCheckInCheckOutAndType(int hotelId, Date checkIn, Date checkOut, int typeRoomId);


    @Query (value = "select * from room where hotel_id = ?1 and is_delete = 0", nativeQuery = true)
    public List<Room> findRoomByHotel (int hotelId);


}
