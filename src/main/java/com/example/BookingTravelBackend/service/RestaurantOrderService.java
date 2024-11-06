package com.example.BookingTravelBackend.service;
import com.example.BookingTravelBackend.entity.RestaurantOrder;
import com.example.BookingTravelBackend.payload.Request.OrderFoodRequest;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;

public interface RestaurantOrderService {
    public RestaurantOrder findById (int id);

    void updateStatusBill(RestaurantOrder order, String active);
    public void save (RestaurantOrder order);
    public OrderFoodResponse order (OrderFoodRequest orderFoodRequest);
}
