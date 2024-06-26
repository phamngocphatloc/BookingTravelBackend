package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CommentPost;
import lombok.Getter;

@Getter
public class PostCommentResponse {
    private int id;
    private UserDetailsResponse user;
    private String comment;

    public PostCommentResponse (CommentPost comment){
        this.id = comment.getId();
        user = new UserDetailsResponse(comment.getUser());
        this.comment = comment.getComment();
    }

}
