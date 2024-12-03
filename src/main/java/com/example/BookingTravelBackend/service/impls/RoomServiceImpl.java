package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.RoomEditRequest;
import com.example.BookingTravelBackend.payload.Request.RoomRequest;
import com.example.BookingTravelBackend.payload.respone.BedRespone;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.RoomRespone;
import com.example.BookingTravelBackend.service.PartnersHotelService;
import com.example.BookingTravelBackend.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final TypeRoomRepository bedRepository;
    private final PartnersHotelService partnersHotelService;
    private final PartnersRepository hotelPartnersRepository;
    private final TypeRoomRepository typeRoomRepository;
    @Override
    public List<Room> selectRoomAllByHotel(Hotel hoel) {
        return hoel.getListRooms();
    }

    @Override
    public HotelRespone CheckRoomNotYet(HotelRespone response, Date checkIn, Date checkOut) {
        List<BookingDetails> bookings = bookingRepository.listBookingByCheckinCheckout(response.getHotelId(),checkIn,checkOut);
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

        Optional<Room> roomexist = roomRepository.findByRoomNameLikeAndHotel("%"+room.getRoomName()+"%",room.getHotelId());

        Hotel hotel = hotelRepository.findById(room.getHotelId()).get();

        TypeRoom bed = bedRepository.findById(room.getTypeRoomId()).get();
        if (bed == null){
            throw new IllegalStateException("Không Tìm Thấy Giường Này");
        }
        if (roomexist.isPresent()){
            Room roomEdit = roomexist.get();
            roomEdit.setTypeRoom(room.getTypeRoom());
            roomEdit.setDescribe(room.getDescribe());
            roomEdit.setPrice(room.getPrice());
            roomEdit.setNumberOfPeople(room.getNumberOfPeople());
            roomEdit.getTypeRoomList().remove(0);
            roomEdit.getTypeRoomList().add(bed);
            roomEdit.setDelete(false);
            roomRepository.save(roomEdit);

        }else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userLogin = (User) authentication.getPrincipal();
            if (hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), room.getHotelId()) == 0) {
                throw new IllegalArgumentException("Bạn Không Phải Đối Tác Khách Sạn Này");
            }
            Room r = room.getRoom(hotel);
            r.getTypeRoomList().add(bed);
            roomRepository.save(r);
        }
    }

    @Override
    public List<BedRespone> selectAllBed() {
        List<BedRespone> list = new ArrayList<>();
        bedRepository.findAll().stream().forEach(item -> {
            list.add(new BedRespone(item));
        });
        return list;

    }

    @Override
    @Transactional
    public RoomRespone updateRoom(RoomEditRequest request) {
        Room roomEdit = roomRepository.findById(request.getRoomId()).get();
        roomEdit.setRoomName(request.getRoomName());
        roomEdit.setTypeRoom(request.getTypeRoom());
        roomEdit.setDescribe(request.getDescribe());
        roomEdit.setPrice(request.getPrice());
        roomEdit.setNumberOfPeople(request.getNumberOfPeople());
        roomEdit.setDelete(false);
        Room roomsaved = roomRepository.save(roomEdit);
        return new RoomRespone(roomsaved);
    }

    @Override
    public RoomRespone selectRoomById(int roomId) {
        return new RoomRespone(roomRepository.findById(roomId).get());
    }

    @Override
    public List<RoomRespone> selectRoomByHotelId(int hotelId) {
        if (partnersHotelService.checkHotelPartnersByHotelId(hotelId)==false){
            throw new IllegalArgumentException("Bạn Không Phải Quản Lý Khách Sạn Này");
        }
        List<Room> listRoom = roomRepository.findRoomByHotel(hotelId);
        List<RoomRespone> response = new ArrayList<>();
        listRoom.stream().forEach(item -> {
            RoomRespone roomRespone = new RoomRespone(item);
            response.add(roomRespone);
        });
        return response;
    }

    @Override
    public RoomRespone deleteRoom(int roomId) {

        Optional<Room> roomexist = roomRepository.findById(roomId);
        if (roomexist.isPresent()){
            Room roomdelete = roomexist.get();
            if (partnersHotelService.checkHotelPartnersByHotelId(roomdelete.getHotelRoom().getHotelId())==false){
                throw new IllegalArgumentException("Bạn Không Phải Quản Lý Khách Sạn Này");
            }
            roomdelete.setDelete(true);
            Room roomSaved = roomRepository.save(roomdelete);
            return new RoomRespone(roomSaved);
        }else{
            throw new IllegalArgumentException("Phòng Không Tồn Tại");
        }
    }


}
