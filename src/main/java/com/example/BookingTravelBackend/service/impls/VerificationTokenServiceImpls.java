package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.Repository.VerificationTokenRepository;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.VerificationToken;
import com.example.BookingTravelBackend.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpls implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    @Override
    public VerificationToken createVerificationToken(User user) {
        VerificationToken existingToken = tokenRepository.findByUserToken(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
            VerificationToken token = new VerificationToken();
            token.setToken(generateRandomString(15));
            token.setUserToken(user);
            token.setExpiryDate(calculateExpiryDate(30)); // 30 minutes
            token.setEnable(true);
            return tokenRepository.save(token);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User verifyUser(String token) {
        VerificationToken verificationToken = getVerificationToken(token);
        if (verificationToken == null) {
            throw new RuntimeException("Invalid token");
        }

        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            throw new RuntimeException("Token has expired");
        }

        System.out.println(verificationToken.getUserToken().getEmail());
        User user = userRepository.findByEmail(verificationToken.getUserToken().getEmail()).get();
        user.setVerify(true);
        userRepository.save(user);
        VerificationToken existingToken = tokenRepository.findByUserToken(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        return user;
    }

    @Override
    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }
}
