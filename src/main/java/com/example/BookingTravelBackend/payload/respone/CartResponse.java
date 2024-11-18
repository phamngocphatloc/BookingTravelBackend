package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CartResponse {
    private int cartId;
    Map<String, CartDetailsResponse> listItems = new HashMap<>();
    private int totalPrice = 0;
    public CartResponse (RestaurantCart cart){
        this.cartId = cart.getCartId();
        Map<String, CartDetails> items = cart.getListItems();
        this.listItems = cart.getListItems().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new CartDetailsResponse(entry.getValue())
                ));
        // Calculate total price by summing up the price of each item
        this.totalPrice = listItems.values().stream()
                .mapToInt(CartDetailsResponse::getPrice) // Assuming CartDetailsResponse has getPrice() method
                .sum();
    }
}
