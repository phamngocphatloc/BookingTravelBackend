package com.example.BookingTravelBackend.Scheduler;

import com.example.BookingTravelBackend.Repository.WalletRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.Repository.HotelPartnersRepository;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.HotelPartners;
import com.example.BookingTravelBackend.entity.Wallet;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BillScheduler {
    private final BillService billService;
    private final RestaurantService restaurantService;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final HotelPartnersRepository hotelPartnersRepository;
    @Scheduled(fixedRate = 5  * 60 * 1000) // Chạy mỗi 5 phút
    public void checkAndCancelUnpaidOrders() {
        billService.cancelUnpaidOrders();
    }

    @Scheduled(fixedRate = 5  * 60 * 1000) // Chạy mỗi 5 phút
    public void checkAndCancelUnpaidFoodOrders() {
        restaurantService.cancelUnpaidOrders();
    }
    @Scheduled(fixedRate = 3 * 60 * 1000) // Chạy mỗi 3 phút
    public void createWallet() {
        // Lấy tất cả người dùng chưa có ví và đối tác khách sạn chưa có ví
        List<User> usersWithoutWallet = userRepository.findByWalletIsNull();
        List<HotelPartners> partnersWithoutWallet = hotelPartnersRepository.findByWalletIsNull();

        // Kết hợp các ví cần tạo cho cả người dùng và đối tác
        List<Wallet> walletsToSave = new ArrayList<>();

        // Tạo ví cho tất cả người dùng chưa có ví
        usersWithoutWallet.forEach(user -> {
            walletsToSave.add(createWalletForUser(user));
        });

        // Tạo ví cho tất cả đối tác khách sạn chưa có ví
        partnersWithoutWallet.forEach(partner -> {
            walletsToSave.add(createWalletForPartner(partner));
        });

        // Lưu đồng loạt các ví nếu danh sách không rỗng
        saveWalletsIfNotEmpty(walletsToSave);
    }

    // Phương thức tạo ví cho người dùng
    private Wallet createWalletForUser(User user) {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal(0)); // Số dư ví ban đầu là 0
        wallet.setCurrency("VNĐ"); // Tiền tệ mặc định là VND
        wallet.setStatus(Wallet.WalletStatus.ACTIVE); // Trạng thái ví là ACTIVE
        wallet.setUser(user);
        wallet.setCreateAt(new Date());
        wallet.setUpdateAt(new Date());
        return wallet;
    }

    // Phương thức tạo ví cho đối tác khách sạn
    private Wallet createWalletForPartner(HotelPartners partner) {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal(0)); // Số dư ví ban đầu là 0
        wallet.setCurrency("VNĐ"); // Tiền tệ mặc định là VND
        wallet.setStatus(Wallet.WalletStatus.ACTIVE); // Trạng thái ví là ACTIVE
        wallet.setPartner(partner);
        wallet.setCreateAt(new Date());
        wallet.setUpdateAt(new Date());
        return wallet;
    }

    // Phương thức kiểm tra danh sách và lưu ví nếu danh sách không rỗng
    private void saveWalletsIfNotEmpty(List<Wallet> walletsToSave) {
        if (!walletsToSave.isEmpty()) {
            walletRepository.saveAll(walletsToSave);
        }
    }


}
