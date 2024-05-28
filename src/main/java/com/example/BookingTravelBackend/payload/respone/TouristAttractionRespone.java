package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import lombok.Getter;

@Getter
public class TouristAttractionRespone {
    private int id;
    private String name;



    public TouristAttractionRespone (TouristAttraction tour){
        this.id = tour.getId();
        this.name = tour.getName();
    }
}
