package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartDetailsRequest {
    private int amount;
    private String size;
    int productId;
    int quantitySet;

    public CartDetails getDetails (Menu menu, RestaurantCart cart){
        CartDetails cartDetails = new CartDetails();
        cartDetails.setAmount(amount);
        cartDetails.setSize(size);
        cartDetails.setProduct(menu);
        cartDetails.setItemCart(cart);
        return cartDetails;
    }
}
