package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.HotelPartners;
import lombok.Getter;
import lombok.Setter;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HotelPartnersResponse {
    private int id;
    private String hotelName;
    private String phone;
    private String email;
    private String postion;
    private List<HotelNameResponse> listHotels = new ArrayList<>();
    public HotelPartnersResponse (HotelPartners parneters){
        this.id = parneters.getId();
        this.hotelName = parneters.getHotelName();
        this.phone = parneters.getPhone();
        this.email = parneters.getEmail();
        parneters.getListHotel().stream().forEach(item -> {
            HotelNameResponse response = new HotelNameResponse();
            response.setId(item.getHotelId());
            response.setAddress(item.getAddress());
            response.setHotelName(parneters.getHotelName());
            listHotels.add(response);
        });
    }

}
