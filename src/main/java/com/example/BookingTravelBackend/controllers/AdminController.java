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
    private final ReportService reportService;
    private final UserService userService;
    private final PartnersHotelService partnersHotelService;
    private final WalletService walletService;


    @GetMapping("get_all_partners")
    public ResponseEntity<HttpRespone> GetAllPartnerts (){
        return ResponseEntity.ok(partnersHotelService.GetAllPartners());
    }
    @GetMapping("get_all_user")
    public ResponseEntity<HttpRespone> getAllUser(){
        return ResponseEntity.ok(userService.GetAllUser());
    }
    @GetMapping("get_all_post")
    public ResponseEntity<HttpRespone> getAllPost(){
        return ResponseEntity.ok(postService.GetAllPost());
    }
    @GetMapping("get_transaction_wallet")
    public ResponseEntity<HttpRespone> GetTransactionWallet(@RequestParam(defaultValue = "") String type){
        return ResponseEntity.ok(walletService.getTransactioWallet(type));
    }

    @GetMapping ("get_all_report_pending")
    public ResponseEntity<HttpRespone> getAllReportPending(){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", reportService.getReport()));
    }
    @PostMapping("complete_transection")
    public ResponseEntity<HttpRespone> CompleteTransection(@RequestParam int id){
        return ResponseEntity.ok(walletService.WalletTransion(id));
    }
    @PostMapping("reportprocessing")
    public ResponseEntity<HttpRespone> getReportProcessing(@RequestParam int id, String status){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", reportService.ReportProcessing(id,status)));
    }

}
