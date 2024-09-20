package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.BillRepository;
import com.example.BookingTravelBackend.Repository.MenuRepository;
import com.example.BookingTravelBackend.Repository.RestaurantRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.Restaurant;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.respone.MenuRestaurantResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpls implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    @Override
    public PaginationResponse LoadProductByOrderId(int orderId, int pageNum, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        Bill bill = billRepository.findById(orderId).get();
        Date checkIn = bill.getBooking().getCheckIn();
        Date checkOut = bill.getBooking().getCheckOut();
        // Lấy ngày hôm nay
        Date today = new Date();

        // Kiểm tra nếu ngày hôm nay nằm trong khoảng từ checkIn đến checkOut
        boolean isTodayInRange = today.compareTo(checkIn) >= 0 && today.compareTo(checkOut) <= 0;

        if (!isTodayInRange) {
            // Thực hiện logic khi hôm nay không nằm trong khoảng từ checkIn đến checkOut
            throw new IllegalArgumentException ("Ngày Hôm Nay Không Có Mã Đơn Này.");
        }

        if (bill.getBooking().getUserBooking().getId() != userLogin.getId()){
            // Thực hiện logic khi Userlogin Khác Với User Đặt Đơn
            throw new IllegalArgumentException ("Bạn Không Phải Người Đặt Đơn Booking Này.");
        }

        if (!bill.getBooking().getStatus().equalsIgnoreCase("active")){
            throw new IllegalArgumentException ("Đơn Hàng Không Hoạt Động");
        }

        if (bill.getBooking().getRoomBooking().getHotelRoom().getRestaurant() == null){
            // Thực hiện logic khi Khách Sạn Chưa Có Nhà Hàng
            throw new IllegalArgumentException ("Khách sạn này không có nhà hàng.");
        }

        Restaurant restaurant = bill.getBooking().getRoomBooking().getHotelRoom().getRestaurant();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Menu> listMenu = menuRepository.findByRestaurant(restaurant.getId(), pageable);
        List<MenuRestaurantResponse> listMenuResponse = new ArrayList<>();
        listMenu.getContent().stream().forEach(item -> {
            listMenuResponse.add(new MenuRestaurantResponse(item));
        });
        PaginationResponse pageResponse = new PaginationResponse(pageNum,pageSize,listMenu.getTotalElements(),listMenu.isLast(),listMenu.getTotalPages(),listMenuResponse);

        return pageResponse;
    }
}
