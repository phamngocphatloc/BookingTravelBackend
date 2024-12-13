package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Like;
import com.example.BookingTravelBackend.entity.Post;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query (value = "select * from post where post.post_title like ?1 or ?1 = ''",nativeQuery = true)
    public Page<Post> findPostBySearch (String search, Pageable pageable);

    @Query(value = "SELECT p.*, \n" +
            "       (\n" +
            "            -- Lượt xem (views) cộng thêm điểm\n" +
            "            COALESCE(p.view_post, 0) * 0.2 + \n" +
            "            \n" +
            "            -- Lượt thích (likes) tăng điểm (giả sử mỗi lượt thích tăng 0.5 điểm)\n" +
            "            (CASE WHEN COALESCE(l.LikesCount, 0) > 0 THEN 0.5 * COALESCE(l.LikesCount, 0) ELSE 0 END) + \n" +
            "            \n" +
            "            -- Bình luận (comments) cộng thêm điểm (2 điểm cho mỗi bình luận)\n" +
            "            COALESCE(c.CommentCount, 0) * 2 + \n" +
            "            \n" +
            "            -- Nếu bài viết là quảng cáo, cộng thêm 1000 điểm\n" +
            "            (CASE WHEN COALESCE(p.is_ad, 0) = 1 THEN 1000 ELSE 0 END) - \n" +
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
            "WHERE p.is_delete = 0 \n" + // Added this condition
            "ORDER BY trending_score DESC;", nativeQuery = true)
    public Page<Post> findTrending(Pageable pageable);


    @Query (value = "SELECT \n" +
            "    COUNT(DISTINCT CASE WHEN l.type = 'like' THEN l.user_id END) AS total_likes,\n" +
            "    COUNT(DISTINCT CASE WHEN l.type = 'dislike' THEN l.user_id END) AS total_dislikes,\n" +
            "    COUNT(c.comment_post_id) AS total_comments\n" +
            "FROM \n" +
            "    post p\n" +
            "LEFT JOIN \n" +
            "    likes l ON p.post_id = l.post_id\n" +
            "LEFT JOIN \n" +
            "    comment_post c ON p.post_id = c.post_id\n" +
            "where p.post_id = ?1\n" +
            "GROUP BY \n" +
            "    p.post_id", nativeQuery = true)
    public Tuple findPostStatusByPostId (int postId);

    @Query(value = "SELECT " +
            "    p.post_id, " +
            "    COUNT(DISTINCT CASE WHEN l.type = 'like' THEN l.user_id END) AS total_likes, " +
            "    COUNT(DISTINCT CASE WHEN l.type = 'dislike' THEN l.user_id END) AS total_dislikes, " +
            "    COUNT(c.comment_post_id) AS total_comments " +
            "FROM " +
            "    post p " +
            "LEFT JOIN " +
            "    likes l ON p.post_id = l.post_id " +
            "LEFT JOIN " +
            "    comment_post c ON p.post_id = c.post_id " +
            "WHERE p.post_id IN :postIds " +
            "GROUP BY p.post_id", nativeQuery = true)
    List<Tuple> findPostStatusesByPostIds(@Param("postIds") List<Integer> postIds);


    @Query (value = "select * from post where user_post = ?1", nativeQuery = true)
    public List<Post> findAllPostByUserId (int userId);

    @Query (value = "SELECT CASE WHEN COUNT(l.id) > 0 THEN 1 ELSE 0 END FROM Likes l WHERE l.user_id = ?1 AND l.post_id = ?2", nativeQuery = true)
    public int checkUserLike (int userId, int postId);
}
