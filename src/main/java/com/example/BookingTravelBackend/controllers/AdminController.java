package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping ("/admin")
public class AdminController {
    private final PostService postService;
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
    @GetMapping("get_all_request_create_partner")
    public ResponseEntity<HttpRespone> getAllRequestCreatePartner(){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.GetAllRequestPartner()));
    }
    @GetMapping("get_all_request_create_partner_by_id")
    public ResponseEntity<HttpRespone> getAllRequestCreatePartnerById(@RequestParam int id){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.GetRequestPartnerById(id)));
    }

    @PostMapping("partner_creation_request_processing")
    public ResponseEntity<HttpRespone> PartnerCreationRequestProcessing(@RequestParam int id, @RequestParam String status){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.PartnerCreationRequestProcessing(id,status)));
    }
    @PostMapping("hotel_creation_request_processing")
    public ResponseEntity<HttpRespone> HotelCreationRequestProcessing(@RequestParam int id, @RequestParam String status){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.HotelCreationRequestProcessing(id,status)));
    }
    @GetMapping ("get_all_request_hotel")
    public ResponseEntity<HttpRespone> GetAllHotelRequest() {

        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.findAllRequestHotelPending()));
    }

    @PostMapping ("blocked_user")
    public ResponseEntity<HttpRespone> BlockedUser (@RequestParam int userId){
        HttpRespone respone = userService.BlookUser(userId);
        return ResponseEntity.ok(respone);
    }
}
