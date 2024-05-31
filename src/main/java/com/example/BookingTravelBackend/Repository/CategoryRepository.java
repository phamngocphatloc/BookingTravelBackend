package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.CategoryBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryBlog,Integer> {
    @Query (value = "select * from blog_category where category_name like ?1", nativeQuery = true)
    CategoryBlog findByCategoryName(String categoryName);
}
