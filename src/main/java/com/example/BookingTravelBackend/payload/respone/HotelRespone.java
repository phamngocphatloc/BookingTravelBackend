package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import java.util.*;

@RequiredArgsConstructor
@Getter
@Setter
public class HotelRespone {


    private int HotelId;

    private String address;

    List<ImageDesbriceRespone> images = new ArrayList<>();

    private String describe;
    List<HotelServiceRespone> listService = new ArrayList<>();

    private List<RoomRespone> listRooms = new ArrayList<>();
    private List<TypeRoomResponse> listTypeRooms = new ArrayList<>();

    private String status;

    private int price;
    private HotelPartnersResponse Partner;



    public HotelRespone (Hotel hotel){
        this.HotelId = hotel.getHotelId();
        this.address = hotel.getAddress();
        hotel.getImages().stream().forEach(item -> {
            images.add(new ImageDesbriceRespone(item));
        });
        this.describe = hotel.getDescribe();
        hotel.getListService().stream().forEach(item -> {
            listService.add(new HotelServiceRespone(item));
        });
        hotel.getListRooms().stream().forEach(item -> {
            listRooms.add(new RoomRespone(item));
        });

        List<RoomRespone> listRoom = listRooms;

        Collections.sort(listRoom, new Comparator<RoomRespone>() {
            @Override
            public int compare(RoomRespone o1, RoomRespone o2) {
                return o1.getPrice() - o2.getPrice();
            }
        });
        if (!listRoom.isEmpty()) {
            this.price = listRoom.get(0).getPrice();
        }else{
            this.price = 0;
        }

        this.Partner = new HotelPartnersResponse(hotel.getPartner());
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
