package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final RoomService roomService;
    @Override
    public PaginationResponse selectHotelByTour(TouristAttraction tour, int pageNum, int pageSize, Date checkIn, Date checkOut) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Hotel> pageHotel = hotelRepository.findByTouristAttraction(tour.getName(),pageable);
        List<HotelRespone> listHotel = new ArrayList<>();
        for (Hotel hotel : pageHotel.getContent()){
            listHotel.add(roomService.CheckRoomNotYet(new HotelRespone(hotel),checkIn,checkOut));
        }

        listHotel.stream().forEach(item -> {
            item.setStatus("full");
            item.getListRooms().stream().forEach(itemRoom -> {
                if (itemRoom.getStatus().equalsIgnoreCase("notyet")){
                    item.setStatus("still");
                }
            });
        });
        return new PaginationResponse(pageNum,pageSize,pageHotel.getTotalElements(),pageHotel.isLast(),pageHotel.getTotalPages(),listHotel);
    }
}
