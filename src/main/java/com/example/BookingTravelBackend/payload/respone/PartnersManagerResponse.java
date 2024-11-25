package com.example.BookingTravelBackend.payload.respone;


import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.entity.PartnersManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PartnersManagerResponse {

    private int id;

    private HotelPartnersResponse hotelPartners;

    private String Position;


    public PartnersManagerResponse (PartnersManager manager){
        this.id = manager.getId();
        this.hotelPartners = new HotelPartnersResponse(manager.getHotelPartners());
        this.Position = manager.getPosition();
        HotelPartners partners = manager.getHotelPartners();

    }
}
