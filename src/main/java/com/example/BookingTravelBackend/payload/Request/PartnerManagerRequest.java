package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerManagerRequest {
    private int partnerId;
    private String email;
    private String position;
}
