package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.BedRepository;
import com.example.BookingTravelBackend.Repository.BookingRepository;
import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.Repository.RoomRepository;
import com.example.BookingTravelBackend.entity.Bed;
import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.payload.Request.RoomRequest;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final BedRepository bedRepository;
    @Override
    public List<Room> selectRoomAllByHotel(Hotel hoel) {
        return hoel.getListRooms();
    }

    @Override
    public HotelRespone CheckRoomNotYet(HotelRespone response, Date checkIn, Date checkOut) {
        List<Booking> bookings = bookingRepository.listBookingByCheckinCheckout(response.getHotelId(),checkIn,checkOut);
        if (bookings.isEmpty()){
            response.getListRooms().stream().forEach(item -> {
                item.setStatus("notyet");
            });
        }else{
            bookings.stream().forEach(item -> {
                response.getListRooms().stream().forEach(itemroom -> {
                    if (item.getRoomBooking().getId() == itemroom.getId()){
                        itemroom.setStatus("Reserved");
                    }
                });
            });

            response.getListRooms().stream().forEach(item -> {
                if (item.getStatus() == null    ){
                    item.setStatus("notyet");
                }
            });
        }
        return response;
    }

    @Override
    @Transactional
    public void addRoom(RoomRequest room) {
        if (hotelRepository.findById(room.getHotelId()).isEmpty()){
            throw new IllegalStateException("Không Tìm Thấy Khách Sạn");
        }
        if (!roomRepository.findByRoomNameLikeAndHotel("%"+room.getRoomName()+"%",room.getHotelId()).isEmpty()){
            throw new IllegalStateException("Phòng Này Đã Tồn Tại");
        }
        Hotel hotel = hotelRepository.findById(room.getHotelId()).get();

        Bed bed = bedRepository.findByBedNameLike("%"+room.getBed()+"%");
        if (bed == null){
            throw new IllegalStateException("Không Tìm Thấy Giường Này");
        }
        Room r = room.getRoom(hotel);
        r.getBed().add(bed);
        roomRepository.save(r);
    }


}
