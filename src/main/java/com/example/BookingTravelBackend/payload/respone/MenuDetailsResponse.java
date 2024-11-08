package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.MenuDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDetailsResponse {
    private int id;
    private String name;
    private String size;
    private int price;
    private int productId;
    private String productName;
    private String img;

    public MenuDetailsResponse (MenuDetails menuDetails){
        this.id = menuDetails.getId();
        this.name = menuDetails.getName();
        this.size = menuDetails.getSize();
        this.price = menuDetails.getPrice();
        this.productId = menuDetails.getProduct().getId();
        this.productName = menuDetails.getProduct().getProductName();
        this.img = menuDetails.getProduct().getImgProduct();
    }
}
