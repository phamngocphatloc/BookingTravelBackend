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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
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
}
