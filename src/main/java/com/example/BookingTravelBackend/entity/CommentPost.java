package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
}
