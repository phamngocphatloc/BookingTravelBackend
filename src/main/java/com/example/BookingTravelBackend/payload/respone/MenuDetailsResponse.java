package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.MenuDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailsResponse {
    private int id;
    private String name;
    private String size;
    private int price;

    public MenuDetailsResponse (MenuDetails menuDetails){
        this.id = menuDetails.getId();
        this.name = menuDetails.getName();
        this.size = menuDetails.getSize();
        this.price = menuDetails.getPrice();
    }
}
