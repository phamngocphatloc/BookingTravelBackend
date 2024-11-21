package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping ("/following")
    public ResponseEntity<HttpRespone> FollowingBUser (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",userService.AllFollowingByUser(principal.getId())));
    }
}
