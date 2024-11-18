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

    public User validateFormAndGetUserLogin(Booking bill) {
        Date today = removeTime(new Date());
        BookingDetails book = bill.getListDetails().get(0);
        Date checkInWithoutTime = removeTime(bill.getCheckIn());
        Date checkOutWithoutTime = removeTime(bill.getCheckOut());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        boolean isTodayInRange = today.compareTo(checkInWithoutTime) >= 0 && today.compareTo(checkOutWithoutTime) <= 0;
        if (!isTodayInRange) {
            throw new IllegalArgumentException("Ngày Hôm Nay Không Có Mã Đơn Này.");
        }

        // So sánh trực tiếp với user login trong cơ sở dữ liệu
        User orderOwner = userRepository.findUserByBillId(bill.getId())
                .orElseThrow(() -> new IllegalArgumentException("Đơn hàng không tồn tại!"));

        if (orderOwner.getId() != userLogin.getId()) {
            throw new IllegalArgumentException("Bạn Không Phải Người Đặt Đơn Booking Này.");
        }

        if (!bill.getStatus().equalsIgnoreCase("active")) {
            throw new IllegalArgumentException("Đơn Hàng Không Hoạt Động");
        }

        if (restaurantRepository.findRestaurantByBillId(bill.getId()).isEmpty()) {
            throw new IllegalArgumentException("Khách Sạn Không Có Nhà Hàng");
        }

        return userLogin;
    }

    @Override
    public PaginationResponse LoadProductByOrderId(int orderId, int pageNum, int pageSize) {
        Booking bill = billRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Đơn hàng không tồn tại!"));

        User userLogin = validateFormAndGetUserLogin(bill);

        Restaurant restaurant = restaurantRepository.findRestaurantByBillId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nhà hàng không tồn tại!"));

        // Tìm kiếm giỏ hàng của người dùng và đơn hàng (chỉ 1 truy vấn)
        restaurantCartRepository.findByUserAndBill(userLogin.getId(), bill.getId())
                .orElseGet(() -> {
                    RestaurantCart cart = new RestaurantCart();
                    cart.setBill(bill);
                    cart.setUserCart(userLogin);
                    return restaurantCartRepository.save(cart);
                });

        // Phân trang và truy vấn danh sách menu
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Menu> listMenu = menuRepository.findByRestaurant(restaurant.getId(), pageable);

        List<MenuRestaurantResponse> listMenuResponse = new ArrayList<>();

        listMenu.getContent().stream().forEach(item -> {
            listMenuResponse.add(new MenuRestaurantResponse(item,true));
        });

        return new PaginationResponse(pageNum, pageSize, listMenu.getTotalElements(),
                listMenu.isLast(), listMenu.getTotalPages(), listMenuResponse);
    }

    @Override
    public MenuRestaurantResponse findById(int billId, int foodId) {
        Booking bill = billRepository.findById(billId).get();
        Restaurant restaurant = restaurantRepository.findRestaurantByBillId(billId).get();
        User userLogin = validateFormAndGetUserLogin(bill);
        Menu menu = menuRepository.findById(foodId).get();

        if (menu.getRestaurantSell().getId() != restaurant.getId()) {
            throw new IllegalArgumentException("Nhà Hàng Không Có Sản Phẩm Này");
        }
        MenuRestaurantResponse response = new MenuRestaurantResponse(menu);
        // Phân trang và truy vấn danh sách menu
        Pageable pageable = PageRequest.of(0, 4);
        Page<Menu> listMenu = menuRepository.findByRestaurant(restaurant.getId(), pageable);

        List<MenuRestaurantResponse> listMenuResponse = listMenu.getContent().stream()
                .map(MenuRestaurantResponse::new)
                .collect(Collectors.toList());
        response.setListRelated(listMenuResponse);
        if (hotelRepository.isAdminHotel(hotelRepository.findHotelByBillId(billId).get().getHotelId(),userLogin.getId())==1){
            response.setAdmin(true);
        }else{
            response.setAdmin(false);
        }
        return response;
    }

    @Override
    public List<OrderFoodResponse> ListOrder(int billId) {
        Booking bill = billRepository.findById(billId).get();
        User userLogin = validateFormAndGetUserLogin(bill);
        List<OrderFoodResponse> listOrder = new ArrayList<>();
        bill.getListOrderFood().stream().forEach(item -> {
            listOrder.add(new OrderFoodResponse(item));
        });
        return listOrder;
    }

}
