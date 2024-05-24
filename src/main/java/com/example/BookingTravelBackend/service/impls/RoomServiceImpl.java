package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.BookingRepository;
import com.example.BookingTravelBackend.Repository.RoomRepository;
import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
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


}
