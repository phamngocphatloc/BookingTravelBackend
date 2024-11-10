package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.PostMediaRepository;
import com.example.BookingTravelBackend.Repository.CommentPostRepository;
import com.example.BookingTravelBackend.Repository.CategoryRepository;
import com.example.BookingTravelBackend.Repository.PostRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
import com.example.BookingTravelBackend.payload.Request.PostMeidaRequest;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.CommentPostResponse;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;
import com.example.BookingTravelBackend.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostServiceImpls implements PostService {
    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final UserRepository userRepository;
    private final CommentPostRepository commentPostRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public PaginationResponse findAllPost(String search, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Post> pagePost = postRepository.findPostBySearch("%"+search+"%",pageable);
        List<PostResponse> listPostResponse = new ArrayList<>();
        pagePost.getContent().stream().forEach(item -> {
            listPostResponse.add(new PostResponse(item));
        });
        PaginationResponse pagePostResponse = new PaginationResponse(pageNum,pageSize,pagePost.getTotalElements(),pagePost.isLast(),pagePost.getTotalPages(),listPostResponse);
        return pagePostResponse;
    }

    @Override
    public PaginationResponse findTrending(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Post> pagePost = postRepository.findTrending(pageable);
        List<PostResponse> listPostResponse = new ArrayList<>();
        pagePost.getContent().stream().forEach(item -> {
            listPostResponse.add(new PostResponse(item));
        });
        PaginationResponse pagePostResponse = new PaginationResponse(pageNum,pageSize,pagePost.getTotalElements(),pagePost.isLast(),pagePost.getTotalPages(),listPostResponse);
        return pagePostResponse;
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).get();
    }

    @Override
    @Transactional
    public PostResponse AddPost(PostRequest postRequest) {
        // Tạo bài viết mới
        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setDatePost(new Date());
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        User user = userRepository.findById(userLogin.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        post.setUserPost(user);
        post.setView(0);

        // Lấy danh mục (giả sử danh mục với ID 2 luôn tồn tại)
        CategoryBlog category = categoryRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        post.setCategory(category);

        // Lưu bài viết
        Post postSaved = postRepository.save(post);

        // Lưu các media liên quan (nếu có)
        if (postRequest.getListMedia() != null) {
            for (PostMeidaRequest item : postRequest.getListMedia()) {
                PostMedia media = new PostMedia();
                media.setPost(postSaved);
                media.setMediaType(item.getMediaType());
                media.setMediaUrl(item.getMediaUrl());
                postMediaRepository.save(media);
                postSaved.getMedia().add(media);
            }
        }

        // Trả về đối tượng PostResponse (đảm bảo phương thức findById trả về PostResponse)
        return new PostResponse(postSaved);
    }



    @Override
    public CommentPostResponse CommentPost(CommentRequest request, User user) {
        Post post = postRepository.findById(request.getPostid()).get();

        CommentPost comment = new CommentPost();
        comment.setComment(request.getComment());
        comment.setCommentPost(post);
        comment.setCreate_At(new Timestamp(System.currentTimeMillis())); // Set timestamp hiện tại);
        comment.setUser(user);
        CommentPostResponse response = new CommentPostResponse(commentPostRepository.save(comment));
        return response;
    }

    @Override
    public List<PostResponse> getAllPost() {
        List<PostResponse> response = new ArrayList<>();
        postRepository.findAll().stream().forEach(item -> {
            response.add(new PostResponse(item));
        });
        return response;
    }
}
