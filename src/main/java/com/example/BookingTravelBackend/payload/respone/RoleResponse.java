package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {
    private String email;
    private String role;

    public RoleResponse(String email, String role) {
        this.email = email;
        this.role = role;
    }
}