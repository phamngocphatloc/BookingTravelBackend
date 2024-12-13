package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostInfoResponse {
    private int postId;
    private int view;
    private Date datePost;
    private String content;

    public PostInfoResponse (Post post){
        this.postId = post.getPostId();
        this.datePost = post.getDatePost();
        this.content = post.getContent();
        this.view = post.getView();
    }
}
