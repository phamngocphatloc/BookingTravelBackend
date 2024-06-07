package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.payload.Request.HotelRequestEdit;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.PostService;
import com.example.BookingTravelBackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/admin")
public class AdminController {
    private final PostService postService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final BillService billService;
    @PostMapping("/addPost")
    public ResponseEntity<HttpRespone> addPost (@RequestBody PostRequest postRequest){
        PostResponse response = postService.AddPost(postRequest);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }

    @GetMapping("/getAllHotelAdmin")
    public ResponseEntity<HttpRespone> getHotelAdmin (){
        List<HotelRespone> page = hotelService.listHotel();
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "Success", page));
    }

    @PutMapping ("/editHotel")
    public ResponseEntity<HttpRespone> editHotel (@RequestBody HotelRequestEdit requestEdit){
        HotelRespone respone = hotelService.editHotel(requestEdit);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",respone));
    }

    @DeleteMapping ("/deleteHotel")
    public ResponseEntity<HttpRespone> deleteHotel (@RequestParam int hotelId){
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"delete success", null));
    }

    @GetMapping ("/getroombyhotel")
    public ResponseEntity<HttpRespone> getRoomByHotel (@RequestParam  int hotelId){
        HotelRespone hotel = hotelService.selectHotelId(hotelId);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",hotel.getListRooms()));
    }

    @GetMapping ("/gethotelid")
    public ResponseEntity<HttpRespone> getHotelById (@RequestParam int hotelId){
        HotelRespone hotel = hotelService.selectHotelId(hotelId);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",hotel));
    }

    @GetMapping ("getRevenue")
    public ResponseEntity<HttpRespone> getRevenue (@RequestParam(value = "hotelId", defaultValue = "0") int hotelId,
                                                   @RequestParam Date dateFrom,
                                                   @RequestParam Date dateTo){
        List<RevenueResponse> response = billService.getRevenua(hotelId,dateFrom,dateTo);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",response));
    }

    @GetMapping ("/getBillByStatus")
    public ResponseEntity<HttpRespone> getBillByStatus (@RequestParam String status, @RequestParam(defaultValue = "0") int hotelId){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", billService.selectBillByStatus(status,hotelId)));
    }

    @PutMapping ("/updateBillStatus")
    public ResponseEntity<HttpRespone> updateBillByStatus (@RequestParam String status, @RequestParam int billId){
        billService.updateStatusBill(status,billId);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", billService.selectBillById(billId)));
    }
}
