package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.entity.Invoice;
import com.example.BookingTravelBackend.entity.TypeRoom;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.TypeRoomRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.PartnersHotelService;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnersHotelServiceImpls implements PartnersHotelService {
    private final InvoiceRepository invoiceRepository;
    private final PartnersRepository hotelPartnersRepository;
    private final TypeRoomRepository typeRoomRepository;
    private final RoomRepository roomRepository;
    @Override
    public List<HotelPartnersResponse> listPartnersByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        List<HotelPartnersResponse> listResponse = new ArrayList<>();
        List<HotelPartners> listPartners = hotelPartnersRepository.ListAllHotelPartnersByUser(userLogin.getId());
        if (listPartners == null){
            throw new IllegalArgumentException("Tài Khoản Không Có Tài Khoản Đối Tác");
        }
        listPartners.stream().forEach(item -> {
            HotelPartnersResponse response = new HotelPartnersResponse(item);
            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public boolean checkHotelPartnersByHotelId(int hotelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        if (hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(),hotelId)==1){
            return true;
        }
        return false;
    }

    @Override
    public List<TypeRoomResponse> selectAllTypeRoomByPartnersId(int partNerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        boolean partnerAdmin = userLogin.getListPartnersManager().stream()
                .anyMatch(item -> item.getId() == partNerId);
        if (!partnerAdmin){
            throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
        }
        List<TypeRoomResponse> response = new ArrayList<>();
        List<TypeRoom> typeRooms = typeRoomRepository.findTypeRoomByPartNers(partNerId);
        typeRooms.stream().forEach(item -> {
            response.add(new TypeRoomResponse(item));
        });
        return response;
    }

    @Override
    public TypeRoomResponse saveTypeRoom(TypeRoomRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Kiểm tra quyền của user
        boolean isPartner = hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), request.getHotelId()) > 0;
        if (!isPartner) {
            throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
        }

        // Tìm typeRoom theo partnerId, typeRoom và price
        Optional<TypeRoom> existingTypeRoom = typeRoomRepository.selectTypeRoomByPartnerAndNameAndPrice(
                request.getPartnerId(),
                request.getTypeRoom(),
                request.getPrice()
        );

        TypeRoom typeRoom;
        if (existingTypeRoom.isEmpty()) {
            typeRoom = new TypeRoom();
            typeRoom.setTypeRoom(request.getTypeRoom());

            // Kiểm tra hotelId có tồn tại
            typeRoom.setPartNerType(hotelPartnersRepository.findById(request.getPartnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Partner không tồn tại")));
            typeRoom.setPrice(request.getPrice());
        } else {
            typeRoom = existingTypeRoom.get();
            typeRoom.setPrice(request.getPrice());
            // Nếu cần, có thể cập nhật thêm thông tin cho typeRoom ở đây
        }

        TypeRoom savedTypeRoom = typeRoomRepository.save(typeRoom);
        return new TypeRoomResponse(savedTypeRoom);
    }

    @Override
    public HttpRespone GetAllPartners() {
        List<HotelPartners> listPartners = hotelPartnersRepository.findAll();
        List<HotelPartnersResponse> responses = new ArrayList<>();
        if (!listPartners.isEmpty()){
            listPartners.stream().forEach(item -> {
                responses.add(new HotelPartnersResponse(item));

            });
        }else {
            throw new IllegalArgumentException("không có Pertner");
        }
        return new HttpRespone(HttpStatus.OK.value(),"success",responses);

    }

    @Override
    public HttpRespone RoomReservationNumber(int hotelId) {
        List<Tuple> results = roomRepository.getRoomBookingsByHotelId(hotelId);
        List<RoomBookingResponse> roomBookingResponses = new ArrayList<>();

        for (Tuple result : results) {
            int roomId = (int) result.get("room_id");
            String roomName = (String) result.get("room_name");
            int totalBookings = (int) result.get("total_bookings");
            roomBookingResponses.add(new RoomBookingResponse(roomId, roomName, totalBookings));
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", roomBookingResponses);
    }

    @Override
    public HttpRespone FindAllInvoiceByHotelId(int hotelId) {
        if (checkHotelPartnersByHotelId(hotelId)==false){
            throw new IllegalArgumentException("Bạn Không Phải Admin Của Khách Sạn Này");
        }
        List<Invoice> listInvoice = invoiceRepository.findAllBookingByHotelId(hotelId);
        List<InvoiceResponse> responses = new ArrayList<>();
        listInvoice.stream().forEach(item -> {
            responses.add(new InvoiceResponse(item));
        });
        return new HttpRespone(HttpStatus.OK.value(), "success", responses);
    }


}

