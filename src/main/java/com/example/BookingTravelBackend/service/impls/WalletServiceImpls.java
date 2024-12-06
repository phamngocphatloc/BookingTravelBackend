package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.WalletRepository;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.Wallet;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.WalletResponse;
import com.example.BookingTravelBackend.service.WalletService;
import com.example.BookingTravelBackend.Repository.HotelPartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WalletServiceImpls implements WalletService {
    private final HotelPartnersRepository hotelPartnersRepository;
    private final WalletRepository walletRepository;
    @Override
    public HttpRespone getWalletByPartner(int partnerId) {
        // Lấy người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        if (hotelPartnersRepository.isManagerOfPartner(userLogin.getId(),partnerId)==0){
            throw new IllegalArgumentException("Bạn Không Phải Admin Partner Này");
        }

        Wallet wallet = walletRepository.findWalletById(partnerId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Tìm Thấy Ví Này");
        });


        return new HttpRespone(HttpStatus.OK.value(), "success", new WalletResponse(wallet));
    }
}
