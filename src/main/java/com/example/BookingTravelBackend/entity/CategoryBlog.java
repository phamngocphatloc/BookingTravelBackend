package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table (name = "BlogCategory")
@Getter
@Setter
@Data
public class CategoryBlog {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "CategoryId")
    private int categoryId;
    @Column (name = "CategoryName",columnDefinition = "nvarchar(255)")
    private String categoryName;

    @OneToMany (mappedBy = "category", fetch = FetchType.EAGER)
    private List<Post> post;
}
