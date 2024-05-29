package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query (value = "select * from post where post.post_title like N'%?1%' or ?1 = ''",nativeQuery = true)
    public Page<Post> findPostBySearch (String search, Pageable pageable);
}
