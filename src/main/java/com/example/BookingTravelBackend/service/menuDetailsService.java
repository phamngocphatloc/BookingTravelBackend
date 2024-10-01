package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.MenuDetails;

public interface menuDetailsService {
    MenuDetails findByMenuIdAndSize (int menuId, String size);
}
