package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.Repository.VerificationTokenRepository;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.VerificationToken;
import com.example.BookingTravelBackend.payload.Request.ForgetPasswordRequest;
import com.example.BookingTravelBackend.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpls implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        // Xóa token cũ nếu có
        tokenRepository.deleteByUserToken(user);

        // Tạo token mới
        VerificationToken token = new VerificationToken();
        token.setToken(generateRandomString(15));
        token.setUserToken(user);
        token.setExpiryDate(calculateExpiryDate(30)); // 30 minutes
        token.setEnable(true);

        // Lưu token mới vào cơ sở dữ liệu
        return tokenRepository.save(token);
    }


    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    @Transactional
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
        VerificationToken existingToken = tokenRepository.findByUserToken(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        user.setVerify(true);
        userRepository.save(user);

        return user;
    }

    @Override
    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }



    @Override
    @Transactional
    public User forgetPassword(ForgetPasswordRequest request) {
        // Lấy verification token từ database
        VerificationToken verificationToken = getVerificationToken(request.getToken());
        if (verificationToken == null) {
            throw new RuntimeException("Invalid token");
        }

        if (!verificationToken.getUserToken().isVerify()){
            throw new RuntimeException("Tài Khoản Bạn Chưa Xác Minh Email");
        }

        // Kiểm tra token có hết hạn hay chưa
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpiryDate().before(calendar.getTime()) || !verificationToken.isEnable()) {
            throw new RuntimeException("Token has expired");
        }

        // Kiểm tra email từ request và token có khớp không
        if (!request.getEmail().equalsIgnoreCase(verificationToken.getUserToken().getEmail())) {
            throw new RuntimeException("Email không khớp với token.");
        }

        // Kiểm tra mật khẩu và mật khẩu xác nhận có trùng khớp không
        if (!request.getPassword().equals(request.getReturnPassword())) {
            throw new RuntimeException("Nhập lại mật khẩu không chính xác");
        }
        User user = userRepository.findByEmail(verificationToken.getUserToken().getEmail()).get();
        // Trường hợp không sử dụng merge sau khi xóa

        // Mã hóa mật khẩu mới và lưu người dùng
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        Optional<User> userOptional = userRepository.findByEmail(verificationToken.getUserToken().getEmail());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(encryptedPassword);
        userRepository.save(user);

        // Xóa token cũ nếu có
        tokenRepository.deleteByUserToken(user);


        return user;
    }
    @Override
    // Phương thức kiểm tra token
    public boolean checkTokenValidity(String token, String email) {
        VerificationToken verificationToken = getVerificationToken(token);
        if (verificationToken == null) {
            throw new RuntimeException("Token không hợp lệ.");
        }

        if (!verificationToken.getUserToken().isVerify()){
            throw new RuntimeException("Tài Khoản Bạn Chưa Xác Minh Email");
        }

        // Kiểm tra email từ request và token có khớp không
        if (!email.equalsIgnoreCase(verificationToken.getUserToken().getEmail())) {
            throw new RuntimeException("Email không khớp với token.");
        }

        // Kiểm tra token có hết hạn hay không
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpiryDate().before(calendar.getTime()) || !verificationToken.isEnable()) {
            throw new RuntimeException("Token đã hết hạn hoặc không còn sử dụng được.");
        }

        return true; // Token hợp lệ
    }
}
