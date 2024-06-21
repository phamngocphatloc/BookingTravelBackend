package com.example.BookingTravelBackend.exception;

public class    NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
