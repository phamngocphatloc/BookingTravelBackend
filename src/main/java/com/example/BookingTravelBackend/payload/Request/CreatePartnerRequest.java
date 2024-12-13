package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.RequesttoCreateHotel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.NestingKind;
import java.util.List;

@Getter
@Setter
@Data
public class CreatePartnerRequest {

    public String partnerName;
    private String hotelName;
    private String phone;
    private String email;
    List<CreateHotelRequest> requestHotel;
    private String businessLicense;
}
