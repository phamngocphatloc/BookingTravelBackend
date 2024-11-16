package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
import com.example.BookingTravelBackend.payload.Request.PostMeidaRequest;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.PostService;
import jakarta.persistence.Tuple;
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
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostServiceImpls implements PostService {
    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final UserRepository userRepository;
    private final CommentPostRepository commentPostRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
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
            // Lấy status của post từ repository
            Tuple Tuplestatus = postRepository.findPostStatusByPostId(item.getPostId());

            // Ép kiểu Tuple và lấy giá trị từ các trường
            int totalLikes = (int) Tuplestatus.get("total_likes");
            int totalDislikes = (int) Tuplestatus.get("total_dislikes");
            int totalComments = (int) Tuplestatus.get("total_comments");

            // Tạo đối tượng PostStatusResponse với các thông tin lấy được
            PostStatusResponse status = new PostStatusResponse(totalLikes, totalDislikes, totalComments);

            // Tạo đối tượng PostResponse từ item và gắn status vào
            PostResponse response = new PostResponse(item);
            response.setPostStatusResponse(status);

            // Thêm response đã được gắn status vào listPostResponse
            listPostResponse.add(response);
        });

// Sau khi lặp xong, listPostResponse sẽ chứa các đối tượng PostResponse đã được cập nhật status

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
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        User user = userRepository.findById(userLogin.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        post.setUserPost(user);
        post.setView(0);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
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
    public PostCommentResponse CommentPost(CommentRequest request, User user) {
        Post post = postRepository.findById(request.getPostid()).get();

        CommentPost comment = new CommentPost();
        comment.setComment(request.getComment());
        comment.setCommentPost(post);
        comment.setCreate_At(new Timestamp(System.currentTimeMillis())); // Set timestamp hiện tại);
        comment.setUser(user);
        if (request.getParentCommentId() != null) {
            CommentPost commentParent = commentPostRepository.findById(request.getParentCommentId()).get();
            comment.setParentComment(commentParent);
        }
        PostCommentResponse response = new PostCommentResponse(commentPostRepository.save(comment));
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

    @Override
    @Transactional
    public int updatePostById(int id) {
        Post post = postRepository.findById(id).get();
        post.setView(post.getView()+1);
        Post postSaved = postRepository.save(post);
        return postSaved.getView();
    }

    @Override
    public PostResponse findPostResponseById(int id) {
        Post post = findById(id);
        Tuple Tuplestatus = postRepository.findPostStatusByPostId(id);

        // Ép kiểu Tuple và lấy giá trị từ các trường
        int totalLikes = (int) Tuplestatus.get("total_likes");
        int totalDislikes = (int) Tuplestatus.get("total_dislikes");
        int totalComments = (int) Tuplestatus.get("total_comments");

        // Tạo đối tượng PostStatusResponse với các thông tin lấy được
        PostStatusResponse status = new PostStatusResponse(totalLikes, totalDislikes, totalComments);

        // Tạo đối tượng PostResponse từ item và gắn status vào
        PostResponse response = new PostResponse(post);
        response.setPostStatusResponse(status);
        return response;
    }

    @Override
    @Transactional
    public int Like(int postId, String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()){
            throw new IllegalArgumentException("Không Tìm Thấy Bài Viết");
        }
        if (postRepository.checkUserLike(userLogin.getId(),postId)==0){
            Like likePost = new Like();
            likePost.setPost(post.get());
            likePost.setUser(userLogin);
            likePost.setType(type);
            likeRepository.save(likePost);
        }else{
            Like likeGet = likeRepository.findByUserLike(userLogin.getId(),postId).get();
            if (likeGet.getType().equalsIgnoreCase(type)) {
                likeRepository.delete(likeGet);
            }else{
                likeGet.setType(type);
                likeRepository.save(likeGet);
            }
        };
        int totalLike = likeRepository.AllLikesByPost(postId,type);
        return totalLike;
    }
}
