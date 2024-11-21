package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CommentPost;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CommentPostResponse {
    private int id;
    private UserInfoResponse user;
    private String comment;
    private Timestamp create_at;

    public CommentPostResponse(CommentPost comment){
        this.id = comment.getId();
        this.user = new UserInfoResponse(comment.getUser());
        this.comment = comment.getComment();
        this.create_at = comment.getCreate_At();
    }
}
