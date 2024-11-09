package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "Post")
@Getter
@Setter
@Data
public class Post {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "PostId")
    private int postId;
    @Column (name = "PostTitle", columnDefinition = "nvarchar(255)")
    private String postTitle;
    @Column (name = "PostImg", columnDefinition =  "nvarchar(500)")
    private String postImg;
    @Column (name = "ViewPost")
    private int view;
    @Column (name = "DatePost")
    private Date datePost;
    @Column (name = "Content", columnDefinition = "nvarchar(2500)")
    private String content;
    @OneToMany (mappedBy = "commentPost", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommentPost> comments = new ArrayList<>();
    @ManyToOne
    @JoinColumn (name = "userPost")
    private User userPost;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostMedia> media;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name ="CategoryId")
    private CategoryBlog category;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
