package com.example.BookingTravelBackend.service;
import com.example.BookingTravelBackend.payload.respone.MenuRestaurantResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;
import com.example.BookingTravelBackend.payload.respone.RoomNameResponse;

import java.util.List;

public interface RestaurantService {
    public PaginationResponse LoadProductByOrderId (int orderId, int pageSize, int pageNum);
    public MenuRestaurantResponse findById (int billId, int foodId);
    public List<OrderFoodResponse> ListOrder (int billId);
    public List<RoomNameResponse> findAllRoomNameByBillId (int billI);
}
