package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.Request.BookingRequest;
import com.example.BookingTravelBackend.payload.Request.HotelRequestEdit;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.Request.RoomEditRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/admin")
public class AdminController {
    private final PostService postService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final BillService billService;
    private final UserService userService;
    private final PartnersHotelService partnersHotelService;

    @GetMapping("get_all_partners")
    public ResponseEntity<HttpRespone> GetAllPartnerts (){
        return ResponseEntity.ok(partnersHotelService.GetAllPartners());
    }

}
