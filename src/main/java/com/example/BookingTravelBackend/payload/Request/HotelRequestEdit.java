package com.example.BookingTravelBackend.payload.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelRequestEdit {
        @NotNull (message = "Vui Lòng Nhập HotelId")
        private int hotelId;

        @NotEmpty(message = "Vui lòng nhập địa chỉ")
        private String address;

        private String describe;
        @NotEmpty(message = "Vui lòng nhập địa điểm du lịch")
        private String tourAttractionName;
}
