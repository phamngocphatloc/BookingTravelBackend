package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    @OneToMany (mappedBy = "commentPost", fetch = FetchType.EAGER)
    private List<CommentPost> comments;
    @ManyToOne
    @JoinColumn (name = "userPost")
    private User userPost;
    @ManyToOne
    @JoinColumn (name ="CategoryId")
    private CategoryBlog category;
}
