package com.example.BookingTravelBackend.service;
import com.example.BookingTravelBackend.payload.respone.MenuRestaurantResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;

import java.util.List;

public interface RestaurantService {
    public PaginationResponse LoadProductByOrderId (int orderId, int pageSize, int pageNum);
    public MenuRestaurantResponse findById (int billId, int foodId);
}
