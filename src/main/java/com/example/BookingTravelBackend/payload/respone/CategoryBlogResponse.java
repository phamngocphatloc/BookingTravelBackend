package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.CategoryBlog;
import lombok.Getter;

@Getter
public class CategoryBlogResponse {
    private int id;
    private String name;

    public CategoryBlogResponse (CategoryBlog categoryBlog){
        this.id = categoryBlog.getCategoryId();
        this.name = categoryBlog.getCategoryName();
    }
}
