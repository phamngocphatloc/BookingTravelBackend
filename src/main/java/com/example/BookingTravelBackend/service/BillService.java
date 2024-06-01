package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.entity.Booking;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.BillRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;

import java.util.List;

public interface BillService {
    public BillResponse Booking (BillRequest request);
    public Bill findById(int id);

    public void updateStatusBill (Bill bill, String status);

    public void cancelUnpaidOrders();
    public PaginationResponse selectBillByUser (User user, int pageNum);

    public BillResponse selectBillById (int id);
}
