package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.PostMedia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class PostResponse {
    private int postId;
    private String postTitle;
    private int view;
    private Date datePost;
    private String content;
    private String img;
    private List<PostCommentResponse> comments;
    private UserDetailsResponse userPost;
    private CategoryBlogResponse category;
    private List<CommentPostResponse> listComment;
    private List<PostMedia> listPostMedia;

    public PostResponse (Post post){
        this.postId = post.getPostId();
        this.postTitle = post.getPostTitle();
        this.datePost = post.getDatePost();
        this.content = post.getContent();
        comments = new ArrayList<>();
        post.getComments().stream().forEach(item -> {
            comments.add(new PostCommentResponse(item));
        });
        userPost = new UserDetailsResponse(post.getUserPost());
        category = new CategoryBlogResponse(post.getCategory());
        this.img = post.getPostImg();
    }
}
