package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.HotelService;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
@Getter
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
