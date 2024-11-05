package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import lombok.Getter;

@Getter
public class CartDetailsResponse {
    private int id;
    private int amount;
    private MenuRestaurantResponse product;
    private String size;
    public CartDetailsResponse (CartDetails cart){
        this.id = cart.getId();
        this.amount = cart.getAmount();
        this.product = new MenuRestaurantResponse(cart.getProduct());
        this.size = cart.getSize();
    }
}
