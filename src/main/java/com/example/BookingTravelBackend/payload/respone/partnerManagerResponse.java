package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class partnerManagerResponse {
    private int id;
    private UserInfoResponse user;
    private String postion;
}
