package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Bed;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
