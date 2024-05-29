package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;

public interface PostService {
    public PaginationResponse findAllPost (String search, int pageNum, int pageSize);
    public Post findById (int id);
}
