package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.RestaurantOrder;
import com.example.BookingTravelBackend.entity.RestaurantOrderDetails;
import com.example.BookingTravelBackend.entity.MenuDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.example.BookingTravelBackend.service.menuDetailsService;
@Getter
@Setter
@RequiredArgsConstructor
public class orderFoodDetailRequest {
    private final menuDetailsService menuDetailsService;
    private int amount;
    private String size;
    private RestaurantOrder ItemOrder;
    private int product;


    public RestaurantOrderDetails getOrderDetails (){
        RestaurantOrderDetails restaurantOrderDetails = new RestaurantOrderDetails();
        restaurantOrderDetails.setAmount(getAmount());
        restaurantOrderDetails.setSize(size);
        MenuDetails menuDetails = menuDetailsService.findById(product);
        restaurantOrderDetails.setProduct(menuDetails);
        return restaurantOrderDetails;
    }
}
