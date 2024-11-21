package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.VerificationToken;
import com.example.BookingTravelBackend.payload.Request.ForgetPasswordRequest;

import java.util.Date;

public interface VerificationTokenService {
    public VerificationToken createVerificationToken (User user);
    public VerificationToken getVerificationToken (String token);
    public User verifyUser (String token);
    public Date calculateExpiryDate (int expiryTimeInMinutes);
    public User forgetPassword (ForgetPasswordRequest token);
    public boolean checkTokenValidity(String token, String email);
}
