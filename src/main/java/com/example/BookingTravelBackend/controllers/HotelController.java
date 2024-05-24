package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.Request.RequestSearchHotel;
import com.example.BookingTravelBackend.payload.respone.HotelRespone;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping ("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;
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
}
