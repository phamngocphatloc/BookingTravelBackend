package com.example.BookingTravelBackend.payload.Request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class HotelRequest {
    @NotEmpty(message = "Vui lòng nhập địa chỉ")
    private String address;

    List<ImageDesbriceRequest> images;
    private String describe;
    @NotEmpty(message = "Vui lòng nhập địa điểm du lịch")
    private String tourAttractionName;

}
