package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ChangePasswordRequest;
import com.example.BookingTravelBackend.payload.Request.LoginRequest;
import com.example.BookingTravelBackend.payload.Request.UserInfoRequest;
import com.example.BookingTravelBackend.payload.Request.UserRequest;
import com.example.BookingTravelBackend.payload.respone.JwtResponse;

public interface UserService {
    JwtResponse login(LoginRequest loginRequest);

    void register(UserRequest userRequest);

    User findByEmail(String email);

    User findById (int id);
    Boolean changePassword(ChangePasswordRequest changePasswordRequest);

    void update(int id, UserInfoRequest userInfoRequest);

}
