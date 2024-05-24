package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;

import java.util.Date;
@Getter
public class HttpRespone {
    private int status;
    private String message;
    private String timestamp;
    private Object data;

    public HttpRespone(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date().toString();
        this.data = data;
    }
}
