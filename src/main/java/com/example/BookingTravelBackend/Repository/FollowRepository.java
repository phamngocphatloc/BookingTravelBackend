package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Integer> {
    @Query (value = "select count(*) from follow where followed_user_id = ?1",nativeQuery = true)
    int AllFollowersByUser (int userId);
}
