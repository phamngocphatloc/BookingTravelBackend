package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ChangePasswordRequest;
import com.example.BookingTravelBackend.payload.Request.LoginRequest;
import com.example.BookingTravelBackend.payload.Request.UserInfoRequest;
import com.example.BookingTravelBackend.payload.Request.UserRequest;
import com.example.BookingTravelBackend.payload.respone.JwtResponse;
import com.example.BookingTravelBackend.payload.respone.UserDetailsResponse;

import java.util.List;

public interface UserService {
    JwtResponse login(LoginRequest loginRequest);

    void register(UserRequest userRequest);

    User findByEmail(String email);

    User findById (int id);
    Boolean changePassword(ChangePasswordRequest changePasswordRequest);

    void update(int id, UserInfoRequest userInfoRequest);

    public List<UserDetailsResponse> selectAll ();

    public UserDetailsResponse updateAdmin (int userId, String role);
    public UserDetailsResponse findUserById(int id);
    public int Follow (int userId);

}
