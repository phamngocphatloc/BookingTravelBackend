package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.BookingTravelBackend.service.UserService;
@RestController
@RequiredArgsConstructor
@RequestMapping ("/profile")
public class ProfileController {
    private final UserService userService;
    @GetMapping ("")
    public ResponseEntity<HttpRespone> findProfile (@RequestParam int id){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",userService.findUserById(id)));
    }

    @PostMapping ("/follow_user")
    public ResponseEntity<HttpRespone> FollowUser (@RequestParam int userId){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",userService.Follow(userId)));
    }
}
