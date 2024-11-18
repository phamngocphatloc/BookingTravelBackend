package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.BookingRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.RevenueResponse;

import java.util.Date;
import java.util.List;

public interface BillService {
    public BillResponse Booking (BookingRequest request, String status);
    public Booking findById(int id);

    public void updateStatusBill (Booking bill, String status);

    public void cancelUnpaidOrders();
    public PaginationResponse selectBillByUser (User user, int pageNum);

    public BillResponse selectBillById (int id);

    public List<RevenueResponse> getRevenua (int hotelId, Date dateFrom, Date dateTo);

    public void updateStatusBill (String status, int billId);
}
