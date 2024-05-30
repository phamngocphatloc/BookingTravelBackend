package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.payload.Request.BillRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;

public interface BillService {
    public BillResponse Booking (BillRequest request);
    public Bill findById(int id);

    public void updateStatusBill (Bill bill, String status);

    public void cancelUnpaidOrders();
}
