package com.example.BookingTravelBackend.service;
import com.example.BookingTravelBackend.payload.Request.MenuRestaurantRequest;
import com.example.BookingTravelBackend.payload.Request.RestaurantRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.payload.Request.OrderFoodDetailPartnerRequest;

import java.util.List;

public interface RestaurantService {
    public PaginationResponse LoadProductByOrderId (int orderId, int pageSize, int pageNum);
    public MenuRestaurantResponse findById (int billId, int foodId);
    public List<OrderFoodResponse> ListOrder (int billId);
    public List<RoomNameResponse> findAllRoomNameByBillId (int billI);
    public HttpRespone checkRestaurantByHotel (int hotelId);
    public HttpRespone createRestaurant (RestaurantRequest request);
    public HttpRespone AddMenu (MenuRestaurantRequest request);
    public HttpRespone getRestaurantById (int restaurantId);
    public HttpRespone findAllMenuByRestaurantId (int restaurantId, int hotelId);
    public HttpRespone AddmenuDetails (OrderFoodDetailPartnerRequest request);
    public HttpRespone getAllMenuDetailsByMenuId (int menuId, int hotelId);
    public HttpRespone DeleteMenuDetail (int menuId, int hotelId);
    public HttpRespone FindAllRestaurantOrderByHotelId (int hotelId);
    public void cancelUnpaidOrders();
    public HttpRespone HandleOrderFood (int hotelId, int orderFoodId, String status);
}
