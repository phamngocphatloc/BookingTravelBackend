package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.Request.BookingUpdateRequest;
import com.example.BookingTravelBackend.payload.Request.TypeRoomRequest;
import com.example.BookingTravelBackend.payload.respone.HotelPartnersResponse;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.service.PartnersHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.BookingTravelBackend.service.BillService;
@RestController
@RequestMapping ("/partners")
@RequiredArgsConstructor
public class PartnersController {
    private final PartnersHotelService partnersHotelService;
    private final BillService billService;
    @GetMapping ("/get_all")
    public ResponseEntity<HttpRespone> ListAllPartnerResponse (){
        List<HotelPartnersResponse> response = partnersHotelService.listPartnersByUserId();
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }
    @GetMapping ("/login_partner")
    public ResponseEntity<HttpRespone> loginHotel (int hotelId){
        if (partnersHotelService.checkHotelPartnersByHotelId(hotelId)==false){
            throw new IllegalArgumentException("Bạn Không Phải Quản Lý Của Khách Sạn Này");
        }else{
            return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "yes"));
        }
    }

    @GetMapping ("list_type")
    public ResponseEntity<HttpRespone> ListTypeRoomByPartner (@RequestParam int partnerId){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",partnersHotelService.selectAllTypeRoomByPartnersId(partnerId)));
    }

    @PostMapping("save_type")
    public ResponseEntity<HttpRespone> SaveTypeRoom (@RequestBody TypeRoomRequest request){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",partnersHotelService.saveTypeRoom(request)));
    }

    @GetMapping("get_all_bill")
    public ResponseEntity<HttpRespone> getAllBill (@RequestParam int hotelId, @RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") String phone){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", billService.SelectBookingByHotelIdAndStatus(hotelId,status,phone)));
    }

    @PutMapping ("update_booking")
    public ResponseEntity<HttpRespone> UpdateBooking (@RequestBody BookingUpdateRequest request){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",billService.updateBillId(request.getBookingId(),request.getStatus())));
    }

}
