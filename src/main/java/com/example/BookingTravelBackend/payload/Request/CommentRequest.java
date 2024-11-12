package com.example.BookingTravelBackend.payload.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String comment;
    private int postid;
    private Integer parentCommentId;
}
