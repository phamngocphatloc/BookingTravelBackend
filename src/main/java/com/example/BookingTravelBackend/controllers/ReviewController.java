package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.FoodReviewRequest;
import com.example.BookingTravelBackend.payload.Request.ReplyFoodReviewRequest;
import com.example.BookingTravelBackend.payload.Request.ReviewRequest;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.service.ReviewService;
import com.example.BookingTravelBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.BookingTravelBackend.service.MenuService;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final MenuService menuService;
    @GetMapping ("/getAll")
    public ResponseEntity<HttpRespone> getAll (){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",reviewService.selectAll()));
    }

    @PostMapping ("/comment")
    public ResponseEntity<HttpRespone> comment (@RequestBody ReviewRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",reviewService.Comment(request,userLogin)));
    }

    @PostMapping ("/reviewFood")
    public ResponseEntity<HttpRespone> ReviewFood (@RequestBody FoodReviewRequest request){
        menuService.review(request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"Đánh Giá Thành Công",null));
    }

    @PostMapping ("/reply")
    public ResponseEntity<HttpRespone> ReviewFood (@RequestBody ReplyFoodReviewRequest request){
        menuService.reply(request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"Đánh Giá Thành Công",null));
    }
}
