package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.payload.respone.JwtResponse;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateToken(String email, Map<String, Object> extraClaims);

    Claims extractAllClaims(String token);

    Boolean isTokenValid(String token, String email);

    String extractEmail(String token);


    JwtResponse generateJwtResponse(String email, List<String> roles);
}
