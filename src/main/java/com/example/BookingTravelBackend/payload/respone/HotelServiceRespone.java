package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.HotelService;
import jakarta.persistence.*;

import java.util.List;

public class HotelServiceRespone {

    private int id;

    private String serviceName;

    private int servicePrice;

    public HotelServiceRespone (HotelService service){
        this.id = service.getId();
        this.serviceName = service.getServiceName();
        this.servicePrice = service.getServicePrice();
    }

}
