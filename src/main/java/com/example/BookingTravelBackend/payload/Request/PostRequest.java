package com.example.BookingTravelBackend.payload.Request;

import com.example.BookingTravelBackend.entity.CategoryBlog;
import com.example.BookingTravelBackend.entity.CommentPost;
import com.example.BookingTravelBackend.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Data
public class PostRequest {
    private Integer userId;
    private String postTitle;
    private String postImg;
    private int view;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    private Date datePost;
    private String content;
    private String categoryName;
}
