package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.HotelService;
import com.example.BookingTravelBackend.entity.ImageDesbrice;
import com.example.BookingTravelBackend.entity.Room;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class HotelRequest {
    private String address;

    List<ImageDesbriceRequest> images;
    private String describe;
    private String tourAttractionName;

}
