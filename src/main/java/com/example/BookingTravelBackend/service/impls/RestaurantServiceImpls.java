package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.respone.MenuRestaurantResponse;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.Repository.HotelRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpls implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final RestaurantCartRepository restaurantCartRepository;
    private final HotelRepository hotelRepository;
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

    public User ValiDateFormAndGetUserLogin (Bill bill) {
        Date today = removeTime(new Date());
        Booking book = bill.getBooking();
        Date checkInWithoutTime = removeTime(book.getCheckIn());
        Date checkOutWithoutTime = removeTime(book.getCheckOut());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).orElseThrow(() ->
                new IllegalArgumentException("User không tồn tại!"));
        boolean isTodayInRange = today.compareTo(checkInWithoutTime) >= 0 && today.compareTo(checkOutWithoutTime) <= 0;

        if (!isTodayInRange) {
            throw new IllegalArgumentException ("Ngày Hôm Nay Không Có Mã Đơn Này.");
        }

        if (userRepository.findUserByBillId(bill.getId()).get().getId() != userLogin.getId()){
            // Thực hiện logic khi Userlogin Khác Với User Đặt Đơn
            throw new IllegalArgumentException ("Bạn Không Phải Người Đặt Đơn Booking Này.");
        }

        if (!book.getStatus().equalsIgnoreCase("active")){
            throw new IllegalArgumentException ("Đơn Hàng Không Hoạt Động");
        }

        if (restaurantRepository.findRestaurantByBillId(bill.getId()).isEmpty()) {
            // Thực hiện logic khi Khách Sạn Chưa Có Nhà Hàng
            throw new IllegalArgumentException ("Khách Sạn Không Có Nhà Hàng");
        }
        return userLogin;
    }

    @Override
    public PaginationResponse LoadProductByOrderId(int orderId, int pageNum, int pageSize) {
        Bill bill = billRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("Đơn hàng không tồn tại!"));

        User userLogin = ValiDateFormAndGetUserLogin(bill);

        Restaurant restaurant = restaurantRepository.findRestaurantByBillId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nhà hàng không tồn tại!"));

        // Kiểm tra xem đã có giỏ hàng chưa, nếu chưa thì tạo mới
        restaurantCartRepository.findByUserAndBill(userLogin.getId(), bill.getId())
                .orElseGet(() -> {
                    RestaurantCart cart = new RestaurantCart();
                    cart.setBill(bill);
                    cart.setUserCart(userLogin);
                    return restaurantCartRepository.save(cart);
                });

        // Lấy danh sách menu theo phân trang
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Menu> listMenu = menuRepository.findByRestaurant(restaurant.getId(), pageable);

        // Chuyển đổi kết quả thành list menu response
        List<MenuRestaurantResponse> listMenuResponse = listMenu.getContent().stream()
                .map(MenuRestaurantResponse::new)
                .collect(Collectors.toList());

        return new PaginationResponse(pageNum, pageSize, listMenu.getTotalElements(),
                listMenu.isLast(), listMenu.getTotalPages(), listMenuResponse);
    }

    @Override
    public MenuRestaurantResponse findById(int billId, int foodId) {
        Bill bill = billRepository.findById(billId).get();
        Restaurant restaurant = restaurantRepository.findRestaurantByBillId(billId).get();
        User userLogin = ValiDateFormAndGetUserLogin(bill);
        Menu menu = menuRepository.findById(foodId).get();

        if (menu.getRestaurantSell().getId() != restaurant.getId()) {
            throw new IllegalArgumentException("Nhà Hàng Không Có Sản Phẩm Này");
        }
        MenuRestaurantResponse response = new MenuRestaurantResponse(menu);
        if (hotelRepository.isAdminHotel(hotelRepository.findHotelByBillId(billId).get().getHotelId(),userLogin.getId())==1){
            response.setAdmin(true);
        }else{
            response.setAdmin(false);
        }
        return response;
    }

    @Override
    public List<OrderFoodResponse> ListOrder(int billId) {
        Bill bill = billRepository.findById(billId).get();
        User userLogin = ValiDateFormAndGetUserLogin(bill);
        List<OrderFoodResponse> listOrder = new ArrayList<>();
        bill.getListOrderFood().stream().forEach(item -> {
            listOrder.add(new OrderFoodResponse(item));
        });
        return listOrder;
    }

}
