package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.CommentPostResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;

import java.util.List;

public interface PostService {
    public PaginationResponse findAllPost (String search, int pageNum, int pageSize);
    public Post findById (int id);

    public PostResponse AddPost (PostRequest postRequest);

    public CommentPostResponse CommentPost (CommentRequest request, User user);

    public List<PostResponse> getAllPost ();
}
