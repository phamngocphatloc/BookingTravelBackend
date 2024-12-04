package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.OrderFoodRequest;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;
import com.example.BookingTravelBackend.Repository.RestaurantCartRepository;
import com.example.BookingTravelBackend.Repository.CartDetailsRepository;
import com.example.BookingTravelBackend.service.PartnerNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.Repository.RestaurantOrderRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.Repository.RestaurantOrderDetailsRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantOrderServiceImpls implements com.example.BookingTravelBackend.service.RestaurantOrderService {
    private final RestaurantOrderRepository restaurantOrderRepository;
    private final BillService billService;
    private final RestaurantOrderDetailsRepository restaurantOrderDetailsRepository;
    private final RestaurantCartRepository cartRepository;
    private final CartDetailsRepository cartDetailsRepository;
    private final UserRepository userRepository;
    private final PartnerNotificationService partnerNotificationService;
    @Override
    public RestaurantOrder findById(int id) {
        return restaurantOrderRepository.findById(id).get();
    }

    @Override
    public void updateStatusBill(RestaurantOrder order, String active) {
        order.setStatus(active);
        restaurantOrderRepository.save(order);
    }

    @Override
    public void save(RestaurantOrder order) {
        restaurantOrderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderFoodResponse order(OrderFoodRequest orderFoodRequest) {
        Booking bill = billService.findById(orderFoodRequest.getBill());
        RestaurantOrder order = orderFoodRequest.getOrder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        order.setUserOrder(userLogin);
        order.setBookingBuyed(bill);
        if (cartRepository.findByUserAndBill(userLogin.getId(),orderFoodRequest.getBill()).isEmpty()){
            throw new IllegalArgumentException("Bạn Chưa Có Giỏ Hàng");
        }
        RestaurantCart cart = cartRepository.findByUserAndBill(userLogin.getId(),orderFoodRequest.getBill()).get();
        if (cart.getListItems().isEmpty()){
            throw new IllegalArgumentException("Bạn Chưa Thêm Sản Phẩm Vào Giỏ Hàng");
        }
        Map<String,RestaurantOrderDetails> listItems = new HashMap<>();
        cart.getListItems().forEach((key,value)->{
            RestaurantOrderDetails details = new RestaurantOrderDetails();
            details.setItemOrder(order);
            details.setSize(value.getSize());
            details.setAmount(value.getAmount());
            details.setProduct(value.getProduct());
            listItems.put(details.getProduct().getId()+details.getSize(),details);
            cartDetailsRepository.deleteCartDetailsByCartIdAndKey(cart.getCartId(),value.getProduct().getId()+value.getSize());
        });
        order.setListItems(listItems);
        restaurantOrderRepository.save(order);
        RestaurantOrder orderd = restaurantOrderRepository.findById(order.getId()).get();
        return new OrderFoodResponse(orderd);
    }

    @Override
    public OrderFoodResponse OrderDetail(int id, int billId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        RestaurantOrder order = findById(id);
        if (order.getUserOrder().getId() != userLogin.getId()){
            throw new IllegalArgumentException("Bạn Không Phải Người Mua Đơn Đồ Ăn Này");
        }
        if (order.getBookingBuyed().getId() != billId){
            throw new IllegalArgumentException("Đơn Đồ Ăn Này Không Phải Nằm Trong Đơn Booking này");
        }
        return new OrderFoodResponse(order);
    }
}

