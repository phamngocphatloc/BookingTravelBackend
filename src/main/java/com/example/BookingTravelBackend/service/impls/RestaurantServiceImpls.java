package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpls implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final RestaurantCartRepository restaurantCartRepository;
    // Tạo một hàm để chỉ lấy phần ngày và bỏ qua phần thời gian
    private Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public User ValiDateFormAndGetUserLogin (int orderId) {
        Bill bill = billRepository.findById(orderId).get();
        Date today = removeTime(new Date());
        Date checkInWithoutTime = removeTime(bill.getBooking().getCheckIn());
        Date checkOutWithoutTime = removeTime(bill.getBooking().getCheckOut());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        boolean isTodayInRange = today.compareTo(checkInWithoutTime) >= 0 && today.compareTo(checkOutWithoutTime) <= 0;

        if (!isTodayInRange) {
            throw new IllegalArgumentException ("Ngày Hôm Nay Không Có Mã Đơn Này.");
        }

        if (bill.getBooking().getUserBooking().getId() != userLogin.getId()){
            // Thực hiện logic khi Userlogin Khác Với User Đặt Đơn
            throw new IllegalArgumentException ("Bạn Không Phải Người Đặt Đơn Booking Này.");
        }

        if (!bill.getBooking().getStatus().equalsIgnoreCase("active")){
            throw new IllegalArgumentException ("Đơn Hàng Không Hoạt Động");
        }

        if (bill.getBooking().getRoomBooking().getHotelRoom().getRestaurant() == null) {
            // Thực hiện logic khi Khách Sạn Chưa Có Nhà Hàng
            throw new IllegalArgumentException ("Khách Sạn Không Có Nhà Hàng");
        }
        return userLogin;
    }

    @Override
    public PaginationResponse LoadProductByOrderId(int orderId, int pageNum, int pageSize) {
        User userLogin = ValiDateFormAndGetUserLogin(orderId);
        Bill bill = billRepository.findById(orderId).get();


        Restaurant restaurant = bill.getBooking().getRoomBooking().getHotelRoom().getRestaurant();
        if (restaurantCartRepository.findByUserAndBill(userLogin.getId(),bill.getId()).isEmpty()){
            RestaurantCart cart = new RestaurantCart();
            cart.setBill(bill);
            cart.setUserCart(userLogin);
            restaurantCartRepository.save(cart);
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Menu> listMenu = menuRepository.findByRestaurant(restaurant.getId(), pageable);
        List<MenuRestaurantResponse> listMenuResponse = new ArrayList<>();
        listMenu.getContent().stream().forEach(item -> {
            listMenuResponse.add(new MenuRestaurantResponse(item));
        });
        PaginationResponse pageResponse = new PaginationResponse(pageNum,pageSize,listMenu.getTotalElements(),listMenu.isLast(),listMenu.getTotalPages(),listMenuResponse);

        return pageResponse;
    }

    @Override
    public MenuRestaurantResponse findById(int billId, int foodId) {
        User userLogin = ValiDateFormAndGetUserLogin(billId);
        MenuRestaurantResponse response = new MenuRestaurantResponse(menuRepository.findById(foodId).get());
        return response;
    }

}
