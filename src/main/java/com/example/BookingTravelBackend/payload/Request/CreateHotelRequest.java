package com.example.BookingTravelBackend.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class CreateHotelRequest {

    public String addRess;
    public String desbrice;
    private int touristAttraction;
    private int partner;
    private List<ImageDesbriceRequest> imageDesbriceRequests = new ArrayList<>();
}
