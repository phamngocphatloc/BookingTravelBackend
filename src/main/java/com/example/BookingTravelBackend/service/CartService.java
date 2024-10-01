package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;

public interface CartService {
        public String addTocart (CartDetails item, int orderId);
        public String removeToCart (CartDetails item, int billId);
        public void save (RestaurantCart cart);
        public String setQtyTocart (CartDetails item, int billId, int quantityset);
        public RestaurantCart findCartByUserIdAndBillId (int userId, int billId);
}
