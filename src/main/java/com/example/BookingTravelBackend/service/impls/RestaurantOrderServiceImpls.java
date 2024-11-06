package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.OrderFoodRequest;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;
import com.example.BookingTravelBackend.Repository.RestaurantCartRepository;
import com.example.BookingTravelBackend.Repository.CartDetailsRepository;
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
        Bill bill = billService.findById(orderFoodRequest.getBill());
        RestaurantOrder order = orderFoodRequest.getOrder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        order.setUserOrder(userLogin);
        order.setBill(bill);
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
}

