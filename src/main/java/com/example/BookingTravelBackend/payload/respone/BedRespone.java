package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Bed;
import lombok.Getter;

@Getter
public class BedRespone {
    private int bedId;
    private String bedName;

    public BedRespone (Bed bed){
        this.bedId = bed.getBedId();
        this.bedName = bed.getBedName();
    }
}
