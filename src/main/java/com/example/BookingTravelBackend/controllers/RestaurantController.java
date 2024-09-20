package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    @GetMapping ("/getmenu")
    public HttpRespone getMenu (@RequestParam int orderId,@RequestParam  int pageNum,@RequestParam int pageSize){
        HttpRespone response = new HttpRespone(HttpStatus.OK.value(),"Success",restaurantService.LoadProductByOrderId(orderId,pageNum,pageSize));
        return response;
    }
}
