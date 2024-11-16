package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Integer> {
    @Query(value = "select count(*) from Likes where post_id in (?1) and type = ?2",nativeQuery = true)
    public int AllLikesByPost (int postId, String type);
    @Query (value = "SELECT * FROM Likes l WHERE l.user_id = ?1 AND l.post_id = ?2", nativeQuery = true)
    public Optional<Like> findByUserLike (int userId, int postId);
}
