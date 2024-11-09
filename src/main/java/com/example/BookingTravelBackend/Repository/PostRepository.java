package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query (value = "select * from post where post.post_title like ?1 or ?1 = ''",nativeQuery = true)
    public Page<Post> findPostBySearch (String search, Pageable pageable);

    @Query (value = "SELECT p.*, \n" +
            "       (\n" +
            "            -- Lượt xem (views) cộng thêm điểm\n" +
            "            (p.view_post * 0.2) + \n" +
            "            \n" +
            "            -- Lượt thích (likes) tăng điểm (giả sử mỗi lượt thích tăng 0.5 điểm)\n" +
            "            (CASE WHEN l.LikesCount > 0 THEN 0.5 * l.LikesCount ELSE 0 END) + \n" +
            "            \n" +
            "            -- Bình luận (comments) cộng thêm điểm (2 điểm cho mỗi bình luận)\n" +
            "            c.CommentCount * 2 + \n" +
            "            \n" +
            "            -- Nếu bài viết là quảng cáo, cộng thêm 1000 điểm\n" +
            "            (CASE WHEN p.is_ad = 1 THEN 1000 ELSE 0 END) - \n" +
            "            \n" +
            "            -- Giảm điểm theo thời gian (số giờ đã trôi qua từ khi đăng)\n" +
            "            DATEDIFF(HOUR, p.created_at, GETDATE()) \n" +
            "       ) AS trending_score\n" +
            "FROM Post p\n" +
            "LEFT JOIN (\n" +
            "    -- Đếm số lượt thích (Likes) cho mỗi bài viết\n" +
            "    SELECT post_id, COUNT(*) AS LikesCount\n" +
            "    FROM Likes\n" +
            "    GROUP BY post_id\n" +
            ") l ON p.post_id = l.post_id\n" +
            "LEFT JOIN (\n" +
            "    -- Đếm số bình luận (Comments) cho mỗi bài viết\n" +
            "    SELECT comment_post.post_id, COUNT(*) AS CommentCount\n" +
            "    FROM comment_post\n" +
            "    GROUP BY comment_post.post_id\n" +
            ") c ON p.post_id = c.post_id\n" +
            "ORDER BY trending_score DESC;", nativeQuery = true)
    public Page<Post> findTrending (Pageable pageable);
}
