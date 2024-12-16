package com.example.BookingTravelBackend.Provider;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public CustomAuthenticationProvider(@Lazy PasswordEncoder passwordEncoder, @Lazy UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> listRoles = new ArrayList<>();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
            listRoles.add(authority);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    listRoles);
            if (user.isVerify()==false){
                throw new RuntimeException("Tài Khoản Chưa Xác Minh");
            }
            System.out.println("Đình Chỉ: "+ user.isLocked());
            if (user.isLocked()==true){
                throw new RuntimeException("Tài Khoản Đã Bị Đình Chỉ Vì Vi Phạm Chính Sách");
            }
            return token;
        }
        throw new RuntimeException("Sai Tài Khoản Hoặc Mật Khẩu");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean valid = authentication.equals(UsernamePasswordAuthenticationToken.class);
        return valid;
    }
}