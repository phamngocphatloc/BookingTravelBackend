package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CommentPost;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostCommentResponse {
    private int id;
    private UserDetailsResponse user;
    private String comment;
    private List<CommentPostResponse> reply = new ArrayList<>();
    public PostCommentResponse (CommentPost comment){
        this.id = comment.getId();
        user = new UserDetailsResponse(comment.getUser());
        this.comment = comment.getComment();
        List<CommentPost> listReply = comment.getReplies();
        // Kiểm tra nếu parentComment không phải là null trước khi lặp qua các phần tử trong đó
        if (listReply != null) {
            listReply.forEach(item -> {
                reply.add(new CommentPostResponse(item));
            });
        }
    }

}
