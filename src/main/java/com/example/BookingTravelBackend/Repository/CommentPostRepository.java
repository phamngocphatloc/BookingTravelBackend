package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPostRepository extends JpaRepository<CommentPost, Integer> {
}
