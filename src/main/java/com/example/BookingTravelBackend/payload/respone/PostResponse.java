package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.PostMedia;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostResponse {
    private int postId;
    private int view;
    private Date datePost;
    private String content;
    private List<PostCommentResponse> comments;
    private UserInfoResponse userPost;
    private CategoryBlogResponse category;
    private List<CommentPostResponse> listComment;
    private List<PostMediaResponse> listPostMedia = new ArrayList<>();
    private PostStatusResponse postStatusResponse;

    public PostResponse (Post post){
        this.postId = post.getPostId();
        this.datePost = post.getDatePost();
        this.content = post.getContent();
        comments = new ArrayList<>();
        this.view = post.getView();
        post.getComments().stream().forEach(item -> {
            if(item.getParentComment() == null) {
                comments.add(new PostCommentResponse(item));
            }
        });
        userPost = new UserInfoResponse(post.getUserPost());
        category = new CategoryBlogResponse(post.getCategory());
        post.getMedia().stream().forEach(item -> {
            PostMediaResponse response = new PostMediaResponse();
            response.setMediaType(item.getMediaType());
            response.setMediaUrl(item.getMediaUrl());
            listPostMedia.add(response);
        });
    }
}
