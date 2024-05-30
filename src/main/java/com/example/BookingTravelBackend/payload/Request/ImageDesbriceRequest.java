package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.Hotel;
import com.example.BookingTravelBackend.entity.ImageDesbrice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDesbriceRequest {
    private String title;
    private String link;

    public ImageDesbrice getImageDesbrice (Hotel hotel){
        ImageDesbrice imageDesbrice = new ImageDesbrice();
        imageDesbrice.setLink(this.link);
        imageDesbrice.setTitle(this.title);
        imageDesbrice.setHotelImage(hotel);
        return imageDesbrice;
    }
}
