package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import lombok.Getter;

@Getter
public class CartDetailsResponse {
    private int id;
    private int amount;
    private MenuDetailsResponse product;
    private String size;
    private int price;

    public CartDetailsResponse (CartDetails cart){
        this.id = cart.getId();
        this.amount = cart.getAmount();
        this.product = new MenuDetailsResponse(cart.getProduct());
        this.size = cart.getSize();
        this.price = product.getPrice()*amount;
    }
}
