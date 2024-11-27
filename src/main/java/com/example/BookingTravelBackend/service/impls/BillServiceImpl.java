package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.BookingDetails;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.BookingRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.RevenueResponse;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.util.HandleSort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.entity.Hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PartnersRepository hotelPartnersRepository;
    @Transactional
    @Override
    public BillResponse Booking(BookingRequest request, String status) {
        // Check available rooms
        int quantityStill = roomRepository.selectCountRoomByCheckInCheckOutAndType(
                request.getHotelId(), request.getCheckIn(), request.getCheckOut(), request.getTypeRoom());
        if (quantityStill < request.getQuantityBook()) {
            throw new IllegalArgumentException("Chỉ còn " + quantityStill + " phòng trống, không đủ để đặt " + request.getQuantityBook() + " phòng.");
        }

        // Check date validity
        if (!request.getCheckOut().after(request.getCheckIn())) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng");
        }

        // Create a new booking
        Booking bill = new Booking();
        bill.setPrice(request.getPrice());
        bill.setPhone(request.getPhone());
        bill.setLastName(request.getLastName());
        bill.setFirstName(request.getFirstName());
        bill.setStatus(status);
        bill.setCheckIn(request.getCheckIn());
        bill.setCheckOut(request.getCheckOut());
        bill.setCreatedAt(new Date());

        // Get the logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findById(((User) authentication.getPrincipal()).getId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không hợp lệ"));
        bill.setUserBooking(userLogin);

        // Fetch available rooms and create booking details
        List<Room> listRoomStill = roomRepository.selectAllRoomByCheckInCheckOutAndType(
                request.getHotelId(), request.getCheckIn(), request.getCheckOut(), request.getTypeRoom());

        if (listRoomStill.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy phòng khả dụng");
        }

        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        int quantityBook = request.getQuantityBook();

        for (int i = 0; i < quantityBook && i < listRoomStill.size(); i++) {
            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setRoomBooking(listRoomStill.get(i));
            bookingDetails.setBooking(bill);
            bookingDetailsList.add(bookingDetails);
        }

        // Attach booking details to the booking
        bill.setListDetails(bookingDetailsList);

        // Save the booking to the database
        Booking billCheck = billRepository.save(bill);

        // Return response
        return new BillResponse(billCheck);
    }

    @Override
    public Booking findById(int id) {
        return billRepository.findById(id).get();
    }


    @Override
    @Transactional
    public void updateStatusBill(Booking bill, String status) {
        bill.setStatus(status);
        billRepository.save(bill);
    }

    @Transactional
    public void cancelUnpaidOrders() {
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
        List<Booking> bills = billRepository.findByStatusPendingAndCreatedAtBefore(thirtyMinutesAgo);

        for (Booking bill : bills) {
            updateStatusBill(bill,"Cancel");
        }
    }

    @Override
    public PaginationResponse selectBillByUser(User user, int pageNum) {
        Sort sort = HandleSort.buildSortProperties("booking_id", "desc");
        Pageable pageable = PageRequest.of(pageNum, 10,sort);
        Page<Booking> pageBill = billRepository.findBookingByUser(user.getId(),pageable);
        List<BillResponse> listResponse = new ArrayList<>();
        pageBill.getContent().stream().forEach(item -> {
            listResponse.add(new BillResponse(item));
        });
        PaginationResponse paginationResponse = new PaginationResponse(pageNum,pageBill.getSize(),pageBill.getTotalElements(),pageBill.isLast(),pageBill.getTotalPages(),listResponse);
        return paginationResponse;
    }

    @Override
    public BillResponse selectBillById(int id) {
        BillResponse response = new BillResponse(findById(id));
        return response;
    }

    @Override
    public List<RevenueResponse> getRevenua(int hotelId, Date dateFrom, Date dateTo) {
        List<RevenueResponse> list = new ArrayList<>();
        billRepository.selectRevenueByHotelAndDate(hotelId,dateFrom,dateTo).stream().forEach(item -> {
            list.add(new RevenueResponse(item,dateFrom,dateTo));
        });
        return list;
    }



    @Override
    @Transactional
    public void updateStatusBill(String status, int billId) {
        Booking bill = billRepository.findById(billId).get();
        bill.setStatus(status);
        billRepository.save(bill);
    }

    @Override
    public List<BillResponse> SelectBookingByHotelIdAndStatus(int hotelId, String status, String phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Kiểm tra quyền
        if (hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), hotelId) == 0) {
            throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
        }

        // Lấy danh sách booking
        List<Booking> listBooking = billRepository.listBookingByHotelAndStatus(hotelId, status,phone);

        // Chuyển đổi sang BillResponse
        return listBooking.stream()
                .map(BillResponse::new)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public BillResponse updateBillId(int bookingId, String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Fetch the booking and check if it exists
        Booking bookingEntity = billRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không Tìm Thấy Đơn Đặt Hàng Này"));

        // Ensure booking has details and get the hotel
        if (bookingEntity.getListDetails().isEmpty() || bookingEntity.getListDetails().get(0).getRoomBooking() == null) {
            throw new IllegalArgumentException("Không có chi tiết phòng trong đơn đặt hàng.");
        }

        Hotel hotel = bookingEntity.getListDetails().get(0).getRoomBooking().getHotelRoom();

        // Check if the user has permission to update the booking
        if (hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), hotel.getHotelId()) == 0) {
            throw new IllegalArgumentException("Bạn Không Có Quền Update Đơn Đặt Hàng Này");
        }

        // Check if status is 'success' and if today's date is on or after the check-out date
        if ("success".equalsIgnoreCase(status)) {
            Date today = new Date(System.currentTimeMillis());  // Get today's date
            Date checkOutDate = bookingEntity.getCheckOut(); // Assuming checkOut is of type java.sql.Date

            // Check if today's date is before the check-out date
            if (today.before(checkOutDate)) {
                throw new IllegalArgumentException("Chưa đến ngày checkout, không thể cập nhật trạng thái thành 'success'.");
            }
        }

        // Update the booking status
        bookingEntity.setStatus(status);
        Booking bookingSaved = billRepository.save(bookingEntity);

        // Return the updated booking response
        return new BillResponse(bookingSaved);
    }




}
