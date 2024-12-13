package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.RequesttoCreateHotel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CreateHotelRespone {
    public int id;
    public String addRess;
    public String desbrice;
    private TouristAttractionRespone touristAttractionRespone;
    private HotelPartnersResponse partner;
    private List<ImageDesbriceRespone> imageDesbriceRespones = new ArrayList<>();
    public CreateHotelRespone(RequesttoCreateHotel requesttoCreateHotel) {
        this.id = requesttoCreateHotel.getId();
        this.addRess = requesttoCreateHotel.getAddRess();
        this.touristAttractionRespone = new TouristAttractionRespone(requesttoCreateHotel.getTouristAttraction());
        this.desbrice = requesttoCreateHotel.getDesbrice();
        if (requesttoCreateHotel.getPartner() != null) {
            this.partner = new HotelPartnersResponse(requesttoCreateHotel.getPartner());
        }
        requesttoCreateHotel.getImageDesbrice().stream().forEach(item -> {
            this.imageDesbriceRespones.add(new ImageDesbriceRespone(item));
        });
    }
}
