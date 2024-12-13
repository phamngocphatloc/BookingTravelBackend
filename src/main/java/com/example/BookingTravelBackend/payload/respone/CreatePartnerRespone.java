package com.example.BookingTravelBackend.payload.respone;


import com.example.BookingTravelBackend.entity.RequesttoCreatePartner;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CreatePartnerRespone {
    private int id;
    public String partnerName;
    private String hotelName;
    private String phone;
    private String email;
    List<CreateHotelRespone> responeHotel;
    private String businessLicense;
    private UserInfoResponse userInfoResponse;

    public CreatePartnerRespone(RequesttoCreatePartner requesttoCreatePartner) {
        this.id = requesttoCreatePartner.getRequestId();
        this.partnerName = requesttoCreatePartner.getPartnerName();
        this.hotelName = requesttoCreatePartner.getHotelName();
        this.phone = requesttoCreatePartner.getPhone();
        this.email = requesttoCreatePartner.getEmail();
        this.businessLicense = requesttoCreatePartner.getBusinessLicense();
        this.responeHotel = new ArrayList<>();
        requesttoCreatePartner.getRequestHotel().stream().forEach(item -> {
            this.responeHotel.add(new CreateHotelRespone(item));
        });
        this.userInfoResponse = new UserInfoResponse(requesttoCreatePartner.getUserRequest());

    }
}
