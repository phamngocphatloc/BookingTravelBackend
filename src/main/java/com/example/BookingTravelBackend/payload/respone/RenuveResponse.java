package com.example.BookingTravelBackend.payload.respone;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RenuveResponse {
    private int month;
    private BigDecimal renuve; // Sử dụng BigDecimal thay vì Long để xử lý số tiền chính xác

    public RenuveResponse(Object[] item) {
        this.month = (int) item[0];
        this.renuve = (BigDecimal) item[1];
    }

    @JsonGetter("renuve")
    public String getRenuveAsPlainString() {
        return this.renuve.toPlainString();  // Trả về chuỗi bình thường
    }
}
