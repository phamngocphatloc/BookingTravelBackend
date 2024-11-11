package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentPostRepository extends JpaRepository<CommentPost, Integer> {
}
