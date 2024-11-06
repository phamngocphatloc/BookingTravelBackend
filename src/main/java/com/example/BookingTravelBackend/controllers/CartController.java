package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.CartDetailsRequest;
import com.example.BookingTravelBackend.payload.respone.CartResponse;
import com.example.BookingTravelBackend.payload.respone.addCartRespone;
import com.example.BookingTravelBackend.service.CartService;
import com.example.BookingTravelBackend.service.MenuService;
import com.example.BookingTravelBackend.service.UserService;
import com.example.BookingTravelBackend.service.menuDetailsService;
import com.example.BookingTravelBackend.entity.MenuDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final MenuService menuService;
    private final menuDetailsService menuDetailsService;

    @GetMapping ("/get_cart")
    public ResponseEntity<CartResponse> cart (@RequestParam int billId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        System.out.println(principal.getId());
        User userLogin = userService.findById(principal.getId());
        RestaurantCart cart = cartService.findCartByUserIdAndBillId(userLogin.getId(),billId);
        return ResponseEntity.status(HttpStatus.OK).body(new CartResponse(cartService.findCartByUserIdAndBillId(userLogin.getId(),billId)));
    }
    @PostMapping ("/add_to_cart")
    public ResponseEntity<addCartRespone> addToCart (@RequestBody CartDetailsRequest cartDetailsRequest, @RequestParam int billId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        MenuDetails menu = menuDetailsService.findByMenuIdAndSize(cartDetailsRequest.getProductId(),cartDetailsRequest.getSize());
        RestaurantCart cart = cartService.findCartByUserIdAndBillId(userLogin.getId(),billId);
        CartDetails item = cartDetailsRequest.getDetails(menu,cart);
        String staus = "";
        if (cartDetailsRequest.getQuantitySet() == 0) {
             staus = cartService.addTocart(item, billId);
        }else{
            staus = cartService.setQtyTocart(item,billId,cartDetailsRequest.getQuantitySet());
        }
        if (staus.equalsIgnoreCase("Thêm Vào Giỏ Hàng Thành Công")) {
            return ResponseEntity.status(HttpStatus.OK).body(new addCartRespone(staus));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new addCartRespone(staus));
        }
    }

    @PostMapping ("/set_qty_to_cart")
    public ResponseEntity<addCartRespone> SetQtyToCart (@RequestBody CartDetailsRequest cartDetailsRequest, @RequestParam int billId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        MenuDetails menu = menuDetailsService.findByMenuIdAndSize(cartDetailsRequest.getProductId(),cartDetailsRequest.getSize());
        RestaurantCart cart = cartService.findCartByUserIdAndBillId(userLogin.getId(),billId);
        CartDetails item = cartDetailsRequest.getDetails(menu,cart);
        String staus = cartService.setQtyTocart(item,billId,cartDetailsRequest.getQuantitySet());
        if (staus.equalsIgnoreCase("Thêm Vào Giỏ Hàng Thành Công")) {
            return ResponseEntity.status(HttpStatus.OK).body(new addCartRespone(staus));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new addCartRespone(staus));
        }
    }

    @DeleteMapping ("remove_to_cart")
    ResponseEntity<String> removeToCart (@RequestBody CartDetailsRequest cartDetailsRequest, @RequestParam int billId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        MenuDetails menu = menuDetailsService.findByMenuIdAndSize(cartDetailsRequest.getProductId(),cartDetailsRequest.getSize());
        RestaurantCart cart = cartService.findCartByUserIdAndBillId(userLogin.getId(),billId);
        String status = cartService.removeToCart(cartDetailsRequest.getDetails(menu,cart), billId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }


}

