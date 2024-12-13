package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.CreateHotelRequest;
import com.example.BookingTravelBackend.payload.Request.CreatePartnerRequest;
import com.example.BookingTravelBackend.payload.Request.ImageDesbriceRequest;
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
    private final CreatePartnerRepository createPartnerRepository;
    private final CreateHotelRepository createHotelRepository;
    private final TouristAttractionRepsitory touristAttractionRepository;
    private final ImageDesbriceRequestRepository imageDesbriceRequestRepository;
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

    public void validateCreatePartnerRequest(CreatePartnerRequest request) {
        if (request.getPartnerName() == null || request.getPartnerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đối tác không được để trống.");
        }
        if (request.getPartnerName().length() > 100) {
            throw new IllegalArgumentException("Tên đối tác không được dài hơn 100 ký tự.");
        }

        if (request.getHotelName() == null || request.getHotelName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách sạn không được để trống.");
        }
        if (request.getHotelName().length() > 100) {
            throw new IllegalArgumentException("Tên khách sạn không được dài hơn 100 ký tự.");
        }

        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        }
        if (!request.getPhone().matches("\\d{10,15}")) { // Kiểm tra số điện thoại (10-15 số)
            throw new IllegalArgumentException("Số điện thoại phải từ 10 đến 15 chữ số.");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống.");
        }
        if (!request.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Kiểm tra định dạng email
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }

        if (request.getRequestHotel() == null || request.getRequestHotel().isEmpty()) {
            throw new IllegalArgumentException("Cần cung cấp ít nhất một yêu cầu khách sạn.");
        }
        for (CreateHotelRequest requestHotel : request.getRequestHotel()){
            if (requestHotel.getImageDesbriceRequests().isEmpty()){
                throw new IllegalArgumentException("Cần cung cấp ít nhất một hình mô tả.");
            }
            if (requestHotel.getAddRess().isEmpty()){
                throw new IllegalArgumentException("vui lòng nhập địa chỉ.");
            }
            if (requestHotel.getDesbrice().isEmpty()){
                throw new IllegalArgumentException("vui lòng nhập mô tả.");
            }
        }

    }


    @Override
    public HttpRespone RequestCreatePartners(CreatePartnerRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        validateCreatePartnerRequest(request);
        RequesttoCreatePartner requestCreatePartner = new RequesttoCreatePartner();
        requestCreatePartner.setPartnerName(request.getPartnerName());
        requestCreatePartner.setHotelName(request.getHotelName());
        requestCreatePartner.setEmail(request.getEmail());
        requestCreatePartner.setUserRequest(userLogin);
        requestCreatePartner.setPhone(request.getPhone());
        requestCreatePartner.setRequestHotel(new ArrayList<>());
        requestCreatePartner.setBusinessLicense(request.getBusinessLicense());
        RequesttoCreatePartner requestSaved = createPartnerRepository.save(requestCreatePartner);
        for (CreateHotelRequest item : request.getRequestHotel()) {
            RequesttoCreateHotel requestHotel = new RequesttoCreateHotel();
            requestHotel.setRequesttoCreatePartner(requestSaved);
            requestHotel.setDesbrice(item.getDesbrice().toString());
            requestHotel.setAddRess(item.getAddRess());
            TouristAttraction touristAttraction = touristAttractionRepository
                    .findById(item.getTouristAttraction())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy địa điểm này"));
            requestHotel.setTouristAttraction(touristAttraction);
            RequesttoCreateHotel requestHotelSaved = createHotelRepository.save(requestHotel);
            for (ImageDesbriceRequest imageDesbriceRequest : item.getImageDesbriceRequests()) {
                ImageDescribeRequest imageRequest = new ImageDescribeRequest();
                imageRequest.setImage(imageDesbriceRequest.getLink());
                imageRequest.setHotelRequest(requestHotelSaved);
                ImageDescribeRequest saved = imageDesbriceRequestRepository.save(imageRequest);
                requestHotelSaved.getImageDesbrice().add(saved);
            }
            requestSaved.getRequestHotel().add(requestHotelSaved);

        }
        return new HttpRespone(HttpStatus.OK.value(), "success", requestSaved);
    }


}

