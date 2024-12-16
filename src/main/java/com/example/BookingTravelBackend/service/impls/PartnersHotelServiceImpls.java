package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.*;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.EmailService;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.PartnersHotelService;
import com.example.BookingTravelBackend.util.TemplateEmail;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
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
    private final UserRepository userRepository;
    private final PartnersRepository partnersRepository;
    private final ParnersManagerRepository parnersManagerRepository;
    private final HotelRepository hotelRepository;
    private final ImageDesbriceRepository imageDesbriceRepository;
    private final HotelPartnersRepository partnerRepository;
    private final EmailService emailService;
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
        if (partnerRepository.isManagerOfPartner(userLogin.getId(), partNerId)==0){
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
    @Transactional
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
        requestCreatePartner.setStatus("pending");
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

    @Override
    public HttpRespone GetAllRequestPartner() {
        List<RequesttoCreatePartner> listRequest = createPartnerRepository.findAll();
        List<CreatePartnerRespone> listRepone = new ArrayList<>();
        for (RequesttoCreatePartner request : listRequest) {
            if (request.getStatus().equalsIgnoreCase("pending")) {
                listRepone.add(new CreatePartnerRespone(request));
            }
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", listRepone);
    }

    @Override
    public HttpRespone GetRequestPartnerById(int id) {
        RequesttoCreatePartner request = createPartnerRepository.findById(id).orElseThrow(()->{
            throw new IllegalArgumentException("Không tìm thấy request này");
        });
        return new HttpRespone(HttpStatus.OK.value(), "success", new CreatePartnerRespone(request));
    }

    @Override
    @Transactional
    public HttpRespone PartnerCreationRequestProcessing(int id, String status) {
        RequesttoCreatePartner request = createPartnerRepository.findById(id).orElseThrow(()->{
            throw new IllegalArgumentException("Không tìm thấy request này");
        });
        if (!request.getStatus().equalsIgnoreCase("Pending")){
            throw new IllegalArgumentException("Yêu cầu này đã được xử lý!!");
        }
        if (status.equalsIgnoreCase("Cancel")) {
            request.setStatus("Cancel");
            String mailTemplate = TemplateEmail.getPartnerRejectionEmail(request);
            try {
                emailService.sendEmail(request.getEmail(), "Thông Báo Kết Quả Yêu Cầu Hợp Tác", mailTemplate);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            request.setStatus("Success");
            String mailTemplate = TemplateEmail.getPartnerApprovalEmail(request);
            try {
                emailService.sendEmail(request.getEmail(), "Thông Báo Kết Quả Yêu Cầu Hợp Tác", mailTemplate);
            }catch (Exception e){
                e.printStackTrace();
            }
            HotelPartners hotelPartners = new HotelPartners();
            hotelPartners.setHotelName(request.getHotelName());
            hotelPartners.setEmail(request.getEmail());
            hotelPartners.setPhone(request.getPhone());
            hotelPartners.setListHotel(new ArrayList<>());
            hotelPartners.setListManager(new ArrayList<>());
            hotelPartners.setListTypeRooms(new ArrayList<>());
            hotelPartners.setListReviews(new ArrayList<>());
            HotelPartners saved = partnersRepository.save(hotelPartners);
            User user = userRepository.findById(request.getUserRequest().getId()).orElseThrow(()->{
                throw new IllegalArgumentException("Không tìm thấy user này");
            });
            PartnersManager partnersManager = new PartnersManager();
            partnersManager.setHotelPartners(saved);
            partnersManager.setUserManager(user);
            partnersManager.setPosition("Owner");
            partnersManager.setDelete(true);
            parnersManagerRepository.save(partnersManager);

            request.getRequestHotel().stream().forEach(item -> {
                Hotel hotel = new Hotel();
                hotel.setPartner(saved);
                hotel.setDelete(false);
                hotel.setImages(new ArrayList<>());
                hotel.setAddress(item.getAddRess());
                hotel.setTouristAttraction(item.getTouristAttraction());
                hotel.setDescribe(item.getDesbrice());
                hotel.setListRooms(new ArrayList<>());
                hotel.setListService(new ArrayList<>());
                hotel.setRestaurant(new Restaurant());
                Hotel hotelSaved = hotelRepository.save(hotel);
                for (ImageDescribeRequest img : item.getImageDesbrice()){
                    ImageDesbrice imageDesbrice = new ImageDesbrice();
                    imageDesbrice.setHotelImage(hotelSaved);
                    imageDesbrice.setLink(img.getImage());
                    imageDesbriceRepository.save(imageDesbrice);
                }
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", new CreatePartnerRespone(createPartnerRepository.save(request)));
    }

    @Override
    public HttpRespone HotelCreationRequestProcessing(int id, String status) {
        RequesttoCreateHotel request = createHotelRepository.findById(id).orElseThrow(()->{
            throw new IllegalArgumentException("Không tìm thấy request này");
        });
        if (!request.getStatus().equalsIgnoreCase("Pending")){
            throw new IllegalArgumentException("Yêu cầu này đã được xử lý!!");
        }
        if (status.equalsIgnoreCase("Cancel")) {
            request.setStatus("Cancel");
            String mailTemplate = TemplateEmail.getHotelRejectionEmail(request);
            try {
                emailService.sendEmail(request.getPartner().getEmail(), "Thông Báo Kết Quả Yêu Cầu Thêm Khách Sạn", mailTemplate);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            request.setStatus("Success");
            String mailTemplate = TemplateEmail.getHotelApprovalEmail(request);
            try {
                emailService.sendEmail(request.getPartner().getEmail(), "Thông Báo Kết Quả Yêu Cầu Thêm Khách Sạn", mailTemplate);
            }catch (Exception e){
                e.printStackTrace();
            }
                Hotel hotel = new Hotel();
                hotel.setPartner(request.getPartner());
                hotel.setDelete(false);
                hotel.setImages(new ArrayList<>());
                hotel.setAddress(request.getAddRess());
                hotel.setTouristAttraction(request.getTouristAttraction());
                hotel.setDescribe(request.getDesbrice());
                hotel.setListRooms(new ArrayList<>());
                hotel.setListService(new ArrayList<>());
                hotel.setRestaurant(new Restaurant());
                Hotel hotelSaved = hotelRepository.save(hotel);
                for (ImageDescribeRequest img : request.getImageDesbrice()){
                    ImageDesbrice imageDesbrice = new ImageDesbrice();
                    imageDesbrice.setHotelImage(hotelSaved);
                    imageDesbrice.setLink(img.getImage());
                    imageDesbriceRepository.save(imageDesbrice);
                }
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", new CreateHotelRespone(createHotelRepository.save(request)));
    }

    public HttpRespone findAllRequestHotelPending (){
        List<CreateHotelRespone> respones = new ArrayList<>();
        List<RequesttoCreateHotel> listRequestHotel = createHotelRepository.findAll();
        if (!listRequestHotel.isEmpty()){
            listRequestHotel.forEach(item -> {
                if ((item.getStatus() != null) && item.getStatus().equalsIgnoreCase("pending")){
                    respones.add(new CreateHotelRespone(item));
                }
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", respones);
    }

    @Override
    public HttpRespone findAllMangerByPartnerId(int partnerId) {
        List<PartnersManager> listManager = parnersManagerRepository.GetAllManagerByPartner(partnerId);
        List<partnerManagerResponse> responses = new ArrayList<>();
        if (!listManager.isEmpty()){
            listManager.stream().forEach(item -> {
                partnerManagerResponse itemresponse = new partnerManagerResponse();
                itemresponse.setUser(new UserInfoResponse(item.getUserManager()));
                itemresponse.setId(item.getId());
                itemresponse.setPostion(item.getPosition());
                responses.add(itemresponse);
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", responses);
    }

    @Override
    public HttpRespone addManager(PartnerManagerRequest request) {
        PartnersManager manager = new PartnersManager();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        HotelPartners managerCheck = hotelPartnersRepository.findById(request.getPartnerId()).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Thấy Partner này");
        });
        boolean check = false;
        for(PartnersManager item : managerCheck.getListManager()) {

            if (item.getUserManager().getId() == userLogin.getId()) {
                if (!item.getPosition().equalsIgnoreCase("owner")) {
                    throw new IllegalArgumentException("Bạn Không Phải Chủ Đối Tác Này");
                }
                check = true;
            }
            if (item.getUserManager().getEmail().equalsIgnoreCase(request.getEmail())) {
                throw new IllegalArgumentException("Đã Tồn Tại Nhân Viên Này");
            }
            if (check == false) {
                throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
            }
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->{
            throw new IllegalArgumentException("Không Tìm Thấy User này");
        });

        manager.setUserManager(user);
        manager.setHotelPartners(managerCheck);
        manager.setPosition(request.getPosition());
        PartnersManager managerSaved = parnersManagerRepository.save(manager);
        partnerManagerResponse response = new partnerManagerResponse();
        response.setPostion(managerSaved.getPosition());
        response.setId(manager.getId());
        response.setUser(new UserInfoResponse(managerSaved.getUserManager()));
        return new HttpRespone(HttpStatus.OK.value(), "success", response);
    }

    @Override
    public HttpRespone deleteManager(int id, int partnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        HotelPartners managerCheck = hotelPartnersRepository.findById(partnerId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Thấy Partner này");
        });
        boolean check = false;
        for(PartnersManager item : managerCheck.getListManager()) {

            if (item.getUserManager().getId() == userLogin.getId()) {
                if (!item.getPosition().equalsIgnoreCase("owner")) {
                    throw new IllegalArgumentException("Bạn Không Phải Chủ Đối Tác Này");
                }
                check = true;
            }
            if (check == false) {
                throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
            }
        }
        PartnersManager managerGet = parnersManagerRepository.findById(id).orElseGet(()->{
            throw new IllegalArgumentException("Không Tìm Thấy Nhân Viên Này");
        });

        managerGet.setDelete(true);
        PartnersManager saved = parnersManagerRepository.save(managerGet);
        partnerManagerResponse response = new partnerManagerResponse();
        response.setPostion(saved.getPosition());
        response.setId(saved.getId());
        response.setUser(new UserInfoResponse(saved.getUserManager()));
        return new HttpRespone(HttpStatus.OK.value(), "success", response);
    }

    @Override
    public HttpRespone findManagerByUserLoginAndPartnerId(int partnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        PartnersManager manager = parnersManagerRepository.findManagerByPartnerAndUser(partnerId,principal.getId()).orElseThrow(()->{
            throw new IllegalArgumentException("Không tìm thấy partner");
        });
        partnerManagerResponse response = new partnerManagerResponse();
        response.setPostion(manager.getPosition());
        response.setId(manager.getId());
        response.setUser(new UserInfoResponse(manager.getUserManager()));
        return new HttpRespone(HttpStatus.OK.value(), "success", response);
    }


}

