package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TouristAttractionController {

    @Autowired
    public TouristAttractionService tourService;

    @GetMapping ("/get_attraction")
    public ResponseEntity<HttpRespone> getAttract (){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", tourService.SelectAll()));
    }
}
