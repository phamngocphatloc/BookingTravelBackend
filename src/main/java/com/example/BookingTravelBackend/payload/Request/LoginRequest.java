package com.example.BookingTravelBackend.payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {
    @Email(message = "Email Không Đúng Định Dạng")
    private String email;
    @NotEmpty (message = "Vui Lòng Nhập Password")
    private String password;
}