package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;

public interface PostService {
    public PaginationResponse findAllPost (String search, int pageNum, int pageSize);
    public Post findById (int id);

    public PostResponse AddPost (PostRequest postRequest);
}
