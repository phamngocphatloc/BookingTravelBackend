package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.ImageDesbrice;
import lombok.Getter;

@Getter
public class ImageDesbriceRespone {
    private int id;
    private String title;
    private String link;

    public ImageDesbriceRespone (ImageDesbrice image){
        this.id = image.getId();
        this.title = image.getTitle();
        this.link = image.getLink();
    }
}
