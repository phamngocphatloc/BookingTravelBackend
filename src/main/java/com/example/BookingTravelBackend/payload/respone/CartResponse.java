package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import com.example.BookingTravelBackend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CartResponse {
    private int cartId;
    Map<String, CartDetailsResponse> listItems = new HashMap<>();

    public CartResponse (RestaurantCart cart){
        this.cartId = cart.getCartId();
        Map<String, CartDetails> items = cart.getListItems();
        this.listItems = cart.getListItems().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new CartDetailsResponse(entry.getValue())
                ));
    }
}
