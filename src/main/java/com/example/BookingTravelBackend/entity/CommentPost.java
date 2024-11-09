package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table (name = "CommentPost")
@Getter
@Setter
@Data
public class CommentPost {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "CommentPostId")
    private int id;
    @ManyToOne
    @JoinColumn (name = "userId")
    private User user;
    @Column (name = "comment",columnDefinition = "nvarchar(500)")
    private String comment;
    @ManyToOne
    @JoinColumn (name = "PostId")
    private Post commentPost;
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentPost parentComment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentMedia> media;
    @Column (name = "created_at")
    private Timestamp create_At;
}
