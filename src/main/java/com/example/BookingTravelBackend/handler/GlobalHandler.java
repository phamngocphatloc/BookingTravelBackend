package com.example.BookingTravelBackend.handler;

import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.BookingTravelBackend.exception.NotFoundException;
import org.springframework.web.context.request.WebRequest;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpRespone> handleGlobalException(Exception ex, WebRequest request) {
        HttpRespone response = new HttpRespone(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpRespone> handleNotFoundException(NotFoundException exc) {
        HttpRespone response = new HttpRespone(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exc.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail generateProblemDetail(HttpStatus httpStatus, String errorMessage) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                httpStatus,
                errorMessage);
        problemDetail.setProperty("Timestamp", System.currentTimeMillis());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpRespone> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        HttpRespone response = new HttpRespone(HttpStatus.BAD_REQUEST.value(), "Vui Lòng Nhập Đủ Thông Tin", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}