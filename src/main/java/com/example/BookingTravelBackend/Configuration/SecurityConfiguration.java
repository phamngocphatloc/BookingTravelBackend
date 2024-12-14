package com.example.BookingTravelBackend.Configuration;

import com.example.BookingTravelBackend.Fillter.JwtFilter;
import com.example.BookingTravelBackend.Provider.CustomAuthenticationProvider;
import com.example.BookingTravelBackend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    @Lazy
    private JwtFilter jwtFilter;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)
            throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF (thường dùng cho API)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless cho các API
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**").hasRole(Constant.ROLE_ADMIN)
                        .requestMatchers("/admin/**").hasRole(Constant.ROLE_HOTELMANAGER)
                        .requestMatchers("/auth/authorization").authenticated()
                        .requestMatchers("/restaurant/**").authenticated()
                        .requestMatchers("/restaurant/paying/**").permitAll()
                        // Cho phép tất cả kết nối WebSocket
                        .requestMatchers("/ws/**").permitAll()  // Đảm bảo cho phép tất cả truy cập WebSocket
                        .anyRequest().permitAll()) // Cho phép tất cả các yêu cầu khác
                .httpBasic(Customizer.withDefaults()) // Sử dụng HTTP Basic cho xác thực
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Thêm JwtFilter vào trước UsernamePasswordAuthenticationFilter
        return httpSecurity.build();
    }

}
