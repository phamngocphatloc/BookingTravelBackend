package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.Request.HotelRequest;
import com.example.BookingTravelBackend.payload.Request.HotelServiceRequest;
import com.example.BookingTravelBackend.payload.Request.RoomRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.HotelService;
import com.example.BookingTravelBackend.service.RoomService;
import com.example.BookingTravelBackend.service.TouristAttractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping ("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final TouristAttractionService touristAttractionService;

    private final RoomService roomService;
    @GetMapping ("/getHotel")
    public ResponseEntity<HttpRespone> getHotel (@RequestParam (value = "search", defaultValue = "") String search,
                                                 @RequestParam (value = "hotelName", defaultValue = "") String hotelName,
                                                 @RequestParam ("pagenum") Optional<Integer> pageNum,
                                                 @RequestParam (value = "checkIn")Date checkIn,
                                                 @RequestParam (value = "checkOut")Date checkOut){
        TouristAttraction tour = touristAttractionService.selectByName(search);
        PaginationResponse page = hotelService.selectHotelByTour(tour,pageNum.orElse(0),6,hotelName,checkIn,checkOut);
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

    @PostMapping ("/addRoom")
    public ResponseEntity<HttpRespone> addRoom (@Valid @RequestBody RoomRequest roomRequest){
        roomService.addRoom(roomRequest);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",roomRequest));
    }

    @PostMapping("/addService")
    public ResponseEntity<HttpRespone> addService (@Valid @RequestBody HotelServiceRequest Request){
        hotelService.addHotelService(Request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",Request));
    }

    @GetMapping ("/getAllHotel")
    public ResponseEntity<HttpRespone> getHotelByCheckInCheckOut (@RequestParam ("pagenum") Optional<Integer> pageNum,
                                                 @RequestParam (value = "checkIn")Date checkIn,
                                                 @RequestParam (value = "checkOut")Date checkOut,
                                                                  @RequestParam (value = "pageSize",defaultValue = "6") int pageSize){
        PaginationResponse page = hotelService.selectHotelByCheckInCheckOut(pageNum.orElse(0),pageSize,checkIn,checkOut);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "Success", page));
    }

    @GetMapping ("/get_hotel_name")
    public ResponseEntity<HttpRespone> getHotelName (@RequestParam String tour,@RequestParam String find){
        List<String> listHotelName = hotelService.findHotelNameByTour(tour,find);
        List< HotelNameResponse> responses = new ArrayList<>();
        listHotelName.stream().forEach(item -> {
            HotelNameResponse response = new HotelNameResponse();
            response.setHotelName(item);
            responses.add(response);
        });
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",responses));
    }




}
