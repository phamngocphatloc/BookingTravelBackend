package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.ImageDesbrice;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.entity.TypeRoom;
import com.example.BookingTravelBackend.payload.Request.HotelRequest;
import com.example.BookingTravelBackend.payload.Request.HotelRequestEdit;
import com.example.BookingTravelBackend.payload.Request.HotelServiceRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final HotelServiceRepository hotelServiceRepository;
    private final HotelPartnersRepository hotelPartnersRepository;
    private final TypeRoomRepository typeRoomRepository;
    private final RoomRepository roomRepository;

    @Override
    public PaginationResponse selectHotelByTour(TouristAttraction tour, int pageNum, int pageSize,String hotelName, Date checkIn, Date checkOut) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Hotel> pageHotel = hotelRepository.findByTouristAttraction(tour.getName(),"%"+hotelName+"%",pageable);
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
        if (tour == null){
            throw  new IllegalStateException("Không Tìm Thấy Địa Điểm Này");
        }
        hotel.setTouristAttraction(tour);
        hotelRepository.save(hotel);
    }

    @Override
    public HotelRespone selectById(int id, Date checkIn, Date checkOut) {
        Hotel hotel = hotelRepository.findById(id).get();
        HotelRespone response = new HotelRespone(hotel);
        roomService.CheckRoomNotYet(response,checkIn,checkOut);
        response.setStatus("full");
        response.getListRooms().stream().forEach(itemRoom -> {
            if (itemRoom.getStatus().equalsIgnoreCase("notyet")){
                response.setStatus("still");
            }
        });
        if (hotel.isDelete() == true){
            response.setStatus("delete");
        }
        List<TypeRoom> listTypeRoom = typeRoomRepository.findTypeRoomByHotel(id);
        List<TypeRoomResponse> listTypeRoomResponse = new ArrayList<>();
        listTypeRoom.stream().forEach(item -> {
            int quantityRoomStill = roomRepository.selectCountRoomByCheckInCheckOutAndType(id,checkIn,checkOut,item.getTypeRoomId());
            TypeRoomResponse typeRoomResponse = new TypeRoomResponse(item);
            typeRoomResponse.setQuantityRoomStill(quantityRoomStill);
            listTypeRoomResponse.add(typeRoomResponse);
        });
        response.setListTypeRooms(listTypeRoomResponse);
        return response;
    }

    @Override
    @Transactional
    public void addHotelService(HotelServiceRequest request) {
        com.example.BookingTravelBackend.entity.HotelService service = hotelServiceRepository.findByServiceName(request.getService());
        if (service==null){
            throw new IllegalStateException("Không Tìm Thấy Service");
        }
        Hotel hotel = hotelRepository.findById(request.getHotelId()).get();
        hotel.getListService().add(service);
        hotelRepository.save(hotel);
    }

    @Override
    public PaginationResponse selectHotelByCheckInCheckOut(int pageNum, int pageSize, Date checkIn, Date checkOut) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Hotel> pageHotel = hotelRepository.findAll(pageable);
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
    @Transactional
    public HotelRespone editHotel(HotelRequestEdit request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId()).get();
        hotel.setAddress(request.getAddress());
        hotel.setDescribe(request.getDescribe());
        Hotel savedHotel = hotelRepository.save(hotel);
        return new HotelRespone(savedHotel);
    }

    @Override
    @Transactional
    public void deleteHotel(int hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).get();
        hotelRepository.delete(hotel);
    }

    @Override
    public List<HotelRespone> listHotel() {
        List<HotelRespone> list = new ArrayList<>();
        hotelRepository.findAll().stream().forEach(item -> {
            list.add(new HotelRespone(item));
        });
        return list;
    }

    @Override
    public HotelRespone selectHotelId(int hotelId) {
        return new HotelRespone(hotelRepository.findById(hotelId).get());
    }

    @Override
    public List<String> findHotelNameByTour(String tour, String hotelNaneFind) {
        return hotelPartnersRepository.findHotelNameByTour("%"+tour+"%", "%"+hotelNaneFind+"%");
    }

    @Override
    public HttpRespone findHotelById(int hotelId) {
        // Kiểm tra xem khách sạn có tồn tại không
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Khách sạn không tồn tại"));

        // Tạo và trả về phản hồi với thông tin khách sạn
        return new HttpRespone(HttpStatus.OK.value(), "success", new HotelRespone(hotel));
    }

}
