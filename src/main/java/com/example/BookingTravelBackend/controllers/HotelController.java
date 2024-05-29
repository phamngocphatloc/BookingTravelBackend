package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.Request.HotelRequest;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.HotelServiceRespone;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.TouristAttractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final TouristAttractionService touristAttractionService;
    @GetMapping ("/getHotel")
    public ResponseEntity<HttpRespone> getHotel (@RequestParam (value = "search", defaultValue = "") String search,
                                                 @RequestParam ("pagenum") Optional<Integer> pageNum,
                                                 @RequestParam (value = "checkIn")Date checkIn,
                                                 @RequestParam (value = "checkOut")Date checkOut){
        TouristAttraction tour = touristAttractionService.selectByName(search);
        PaginationResponse page = hotelService.selectHotelByTour(tour,pageNum.orElse(0),8,checkIn,checkOut);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "Success", page));
    }

    @GetMapping ("/getService")
    public ResponseEntity<HttpRespone> getService (@RequestParam ("type")String type){
        List<HotelServiceRespone> listService = new ArrayList<>();
        switch (type){
            case "free":
                listService = hotelService.selectServiceHotelFree();
                break;
            case "luxury":
                listService = hotelService.selectServiceHotelLuxry();
                break;
            case "vip":
                listService = hotelService.selectServiceHotelVip();
                break;
            default:
                throw new IllegalStateException("Vui Lòng Chọn Loại Hợp Lệ");
        }
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",listService));
    }

    @PostMapping ("/addHotel")
    public ResponseEntity<HttpRespone> addHotel (@Valid @RequestBody HotelRequest hotelRequest){
        hotelService.addHotel(hotelRequest);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",hotelRequest));
    }

    @GetMapping ("/getHotelById")
    public ResponseEntity<HttpRespone> getHotelById (@RequestParam("id") int id,
                                                     @RequestParam (value = "checkIn")Date checkIn,
                                                     @RequestParam (value = "checkOut")Date checkOut){
        HotelRespone response = hotelService.selectById(id,checkIn,checkOut);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "Success", response));
    }
}
