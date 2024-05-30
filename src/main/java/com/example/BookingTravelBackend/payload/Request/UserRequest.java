package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
public class UserRequest {
    private Integer id;
    @NotEmpty (message = "Vui Lòng Nhập FullName")
    private String fullName;
    @Email (message = "Email Không Đúng Định Dạng")
    private String email;
    @NotEmpty (message = "Vui Lòng Nhập Số Điện Thoại")
    private String phone;
    @NotEmpty (message = "Vui Lòng Nhập Mật Khẩu")
    private String password;
    private Role role;
    @NotEmpty (message = "Vui Lòng Nhập Địa Chỉ")
    private String address;
    @NotEmpty (message = "Vui Lòng Nhập Tỉnh Thành Phố")
    private String city;
    @NotEmpty (message = "Vui Lòng Nhập Quận Huyện")
    private String district;
    @NotEmpty (message = "Vui Lòng Nhập Phường Xã")
    private String ward;



}
