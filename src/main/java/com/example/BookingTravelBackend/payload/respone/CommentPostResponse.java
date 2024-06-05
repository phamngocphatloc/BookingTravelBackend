package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CommentPost;
import lombok.Getter;

@Getter
public class CommentPostResponse {
    private int id;
    private UserDetailsResponse user;
    private String comment;

    public CommentPostResponse(CommentPost comment){
        this.id = comment.getId();
        this.user = new UserDetailsResponse(comment.getUser());
        this.comment = comment.getComment();
    }
}
