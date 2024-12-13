package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.*;

import java.util.List;

public interface PostService {
    public PaginationResponse findAllPost (String search, int pageNum, int pageSize);
    public PaginationResponse findTrending (int pageNum, int pageSize);
    public Post findById (int id);

    public PostResponse AddPost (PostRequest postRequest);

    public PostCommentResponse CommentPost (CommentRequest request, User user);

    public List<PostResponse> getAllPost ();

    public int updatePostById (int id);
    public PostResponse findPostResponseById (int id);
    public int Like (int postId, String type);
    public HttpRespone GetAllPost();
    public void DeletePost (int id);
    public HttpRespone report (int postId);
}
