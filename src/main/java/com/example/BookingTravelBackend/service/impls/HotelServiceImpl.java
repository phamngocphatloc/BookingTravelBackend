package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.Repository.TouristAttractionRepsitory;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.ImageDesbrice;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.Request.HotelRequest;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.HotelServiceRespone;
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
    private final TouristAttractionRepsitory touristAttractionRepsitory;
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

    @Override
    public List<HotelServiceRespone> selectServiceHotelFree() {
        List<HotelServiceRespone> listResponse = new ArrayList<>();
        hotelRepository.selectServiceHotelFree().stream().forEach(item -> {
            listResponse.add(new HotelServiceRespone(item));
        });
        return listResponse;
    }

    @Override
    public List<HotelServiceRespone> selectServiceHotelLuxry() {
        List<HotelServiceRespone> listResponse = new ArrayList<>();
        hotelRepository.selectServiceHotelLuxury().stream().forEach(item -> {
            listResponse.add(new HotelServiceRespone(item));
        });
        return listResponse;
    }

    @Override
    public List<HotelServiceRespone> selectServiceHotelVip() {
        List<HotelServiceRespone> listResponse = new ArrayList<>();
        hotelRepository.selectServiceHotelVip().stream().forEach(item -> {
            listResponse.add(new HotelServiceRespone(item));
        });
        return listResponse;
    }

    @Override
    public void addHotel(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setAddress(hotelRequest.getAddress());
        hotel.setDescribe(hotelRequest.getDescribe());
        List<ImageDesbrice> listImageDesbrices = new ArrayList<>();
        hotelRequest.getImages().stream().forEach(item -> {
            listImageDesbrices.add(item.getImageDesbrice(hotel));
        });
        hotel.setImages(listImageDesbrices);
        TouristAttraction tour = touristAttractionRepsitory.findByNameLike(hotelRequest.getTourAttractionName());
        hotel.setTouristAttraction(tour);
        hotelRepository.save(hotel);
    }
}
