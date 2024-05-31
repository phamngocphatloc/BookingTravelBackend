package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.BillRepository;
import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.Repository.RoomRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.BillRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import com.example.BookingTravelBackend.service.BillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    @Override
    public BillResponse Booking(BillRequest request) {
        Bill bill = new Bill();
        bill.setPrice(request.getPrice());
        bill.setPhone(request.getPhone());
        bill.setLastName(request.getLastName());
        bill.setFirstName(request.getFirstName());
        Booking booking = new Booking();
        Room roomBooking = roomRepository.findById(request.getBooking().getRoomBookingId()).get();
        User user = userRepository.findById(request.getBooking().getUserBookingId()).get();
        booking.setRoomBooking(roomBooking);
        booking.setUserBooking(user);
        booking.setStatus("pending");
        booking.setCheckIn(request.getBooking().getCheckIn());
        booking.setCheckOut(request.getBooking().getCheckOut());
        bill.setBooking(booking);
        bill.setCreatedAt(new Date());
        Bill billCheck = billRepository.save(bill);
        return new BillResponse(billCheck);
    }


    @Override
    public Bill findById(int id) {
        return billRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void updateStatusBill(Bill bill, String status) {
        bill.getBooking().setStatus(status);
        billRepository.save(bill);
    }

    @Transactional
    public void cancelUnpaidOrders() {
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
        List<Bill> bills = billRepository.findByStatusPendingAndCreatedAtBefore(thirtyMinutesAgo);

        for (Bill bill : bills) {
            updateStatusBill(bill,"Cancel");
        }
    }
}