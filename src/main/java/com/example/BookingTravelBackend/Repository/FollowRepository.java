package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Integer> {
    @Query (value = "select count(*) from follow where followed_user_id = ?1",nativeQuery = true)
    int AllFollowersByUser (int userId);

    @Query (value = "SELECT CASE WHEN COUNT(f.id) > 0 THEN 1 ELSE 0 END FROM Follow f WHERE f.followed_user_id = ?1 AND f.follower_id = ?2",nativeQuery = true)
    int CheckFollow (int User, int FollowerId);

    @Query (value = "select * from follow f WHERE f.followed_user_id = ?1 AND f.follower_id = ?2", nativeQuery = true)
    Follow findFollowByUserAndUserFollow (int userId, int followId);
}
