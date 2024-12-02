package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.MenuRestaurantRequest;
import com.example.BookingTravelBackend.payload.Request.RestaurantRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

    @Override
    public List<RoomNameResponse> findAllRoomNameByBillId(int billI) {
        List<RoomNameResponse> listRoom = new ArrayList<>();
        billRepository.getAllRoomByBooking(billI).stream().forEach(item -> {
            RoomNameResponse roomName = new RoomNameResponse(item);
            listRoom.add(roomName);
        });
        return listRoom;
    }

    @Override
    public HttpRespone checkRestaurantByHotel(int hotelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        if (hotelRepository.isAdminHotel(hotelId,userLogin.getId())==0){
            throw new AccessDeniedException("Bạn Không Phải Admin Khách Sạn Này");
        }
        if (restaurantRepository.checkRestaurantByHotel(hotelId)==1){
            return new HttpRespone(HttpStatus.OK.value(), "success", "yes");
        }else{
            return new HttpRespone(HttpStatus.OK.value(), "success", "no");
        }
    }


    @Override
    @Transactional
    public HttpRespone createRestaurant(RestaurantRequest request) {
        // Lấy thông tin người dùng hiện tại từ Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Kiểm tra xem người dùng có phải là admin của khách sạn này không
        if (hotelRepository.isAdminHotel(request.getHotelRestaurantId(), userLogin.getId()) == 0) {
            throw new AccessDeniedException("Bạn Không Phải Admin Khách Sạn Này");
        }

        // Kiểm tra xem khách sạn đã có nhà hàng hay chưa
        if (restaurantRepository.checkRestaurantByHotel(request.getHotelRestaurantId()) == 1) {
            throw new IllegalArgumentException("Khách Sạn Này Đã Cố Nhà Hàng");
        }

        // Khởi tạo đối tượng Restaurant và thiết lập các thuộc tính
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantImg(request.getRestaurantImg());
        restaurant.setRestaurantName(request.getRestaurantName());

        // Lấy thông tin khách sạn từ hotelRepository bằng phương thức an toàn
        Hotel hotelRestaurant = hotelRepository.findById(request.getHotelRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Khách Sạn Không Tồn Tại"));

        restaurant.setHotelRestaurant(hotelRestaurant);
        restaurant.setActive(true);
        restaurant.setAuthentic(false);
        restaurant.setListMenu(new ArrayList<>());

        // Lưu nhà hàng vào cơ sở dữ liệu
        Restaurant restaurantSaved = restaurantRepository.save(restaurant);

        // Trả về thông tin nhà hàng đã lưu
        return new HttpRespone(HttpStatus.OK.value(), "success", new RestaurantResponse(restaurantSaved));
    }

    @Override
    public HttpRespone AddMenu(MenuRestaurantRequest request) {
        // Lấy thông tin người dùng hiện tại từ Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Kiểm tra xem người dùng có phải là admin của khách sạn này không
        if (hotelRepository.isAdminHotel(request.getHotelId(), userLogin.getId()) == 0) {
            throw new AccessDeniedException("Bạn Không Phải Admin Khách Sạn Này");
        }

        // Kiểm tra xem khách sạn đã có nhà hàng hay chưa
        if (restaurantRepository.checkRestaurantByHotel(request.getHotelId()) == 0) {
            throw new IllegalArgumentException("Khách Sạn Này Chưa Có Nhà Hàng");
        }

        Restaurant restaurantSell = restaurantRepository.findRestaurantByBillId(request.getRestaurantSellId())
                .orElseThrow(() -> new IllegalArgumentException("Nhà Hàng Không Tồn Tại"));
        Menu menu = new Menu();
        menu.setDescription(request.getDescription());
        menu.setRestaurantSell(restaurantSell);
        menu.setImgProduct(request.getImgProduct());
        menu.setListItems(new ArrayList<>());
        menu.setPrice(request.getPrice());
        menu.setProductName(request.getProductName());
        menu.setMenuRestaurantReviews(new ArrayList<>());
        Menu menuSaved = menuRepository.save(menu);
        return new HttpRespone(HttpStatus.OK.value(), "success", new MenuRestaurantResponse(menuSaved));
    }

    @Override
    public HttpRespone getRestaurantById(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Nhà Hàng Không Tồn Tại"));
        restaurant.setListMenu(menuRepository.findAllByRestaurant(restaurantId));
        return new HttpRespone(HttpStatus.OK.value(), "success", new RestaurantResponse(restaurant));
    }

    @Override
    public HttpRespone findAllMenuByRestaurantId(int restaurantId, int hotelId) {
        // Lấy thông tin người dùng hiện tại từ Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        // Kiểm tra xem người dùng có phải là admin của khách sạn này không
        if (hotelRepository.isAdminHotel(hotelId, userLogin.getId()) == 0) {
            throw new AccessDeniedException("Bạn Không Phải Admin Khách Sạn Này");
        }

        // Kiểm tra xem khách sạn đã có nhà hàng hay chưa
        if (restaurantRepository.checkRestaurantByHotel(hotelId) == 0) {
            throw new IllegalArgumentException("Khách Sạn Này Chưa Có Nhà Hàng");
        }

        List<Menu> listMenu = menuRepository.findAllByRestaurant(restaurantId);
        List<MenuRestaurantResponse> listResponse = new ArrayList<>();
        if (!listMenu.isEmpty()){
            listMenu.stream().forEach(item -> {
                listResponse.add(new MenuRestaurantResponse(item));
            });
        }
     return new HttpRespone(HttpStatus.OK.value(), "success", listResponse);
    }


}
