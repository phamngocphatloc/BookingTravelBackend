package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class UserDetailsResponse {
    private int userId;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String district;
    private String ward;

    private String fullName;

    private String role;

    private boolean verify;


    public UserDetailsResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.district = user.getDistrict();
        this.ward = user.getWard();
        this.fullName = user.getFullName();
        this.role = user.getRole().getRoleName();
        this.verify = user.isVerify();
    }
}