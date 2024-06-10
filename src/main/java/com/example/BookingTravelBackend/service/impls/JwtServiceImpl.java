package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.util.Constant;
import com.example.BookingTravelBackend.payload.respone.JwtResponse;
import com.example.BookingTravelBackend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final long accessTokenDuration = 30 * 24 * 60 * 60 * 1000L;
    @Value("${token.secrect.keys}")
    private String JWT_SECRET;

    private Key getSigningKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
        return key;
    }

    @Override
    public String generateToken(String email, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenDuration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    @Override
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public Boolean isTokenValid(String token, String email) {
        final String emailInsideToken = extractEmail(token);
        if (emailInsideToken.equals(email) && !extractExpiration(token).before(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public JwtResponse generateJwtResponse(String email, List<String> roles) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(Constant.LIST_ROLE_KEY, roles);
        String accessToken = generateToken(email, extraClaims);
        // RefreshToken refreshToken = refreshTokenService.processRefreshToken(email);
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                // .refreshToken(refreshToken.getRefreshToken())
                .build();
        return jwtResponse;
    }
}