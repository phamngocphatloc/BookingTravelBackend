package com.example.BookingTravelBackend.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserInfoRequest {
    private int userId;
    private String phone;
    private String address;
    private String fullname;
    private String city;
    private String district;
    private String ward;
    private String avatar; 
}
