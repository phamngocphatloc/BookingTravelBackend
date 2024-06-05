package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.CategoryRepository;
import com.example.BookingTravelBackend.Repository.CommentPostRepository;
import com.example.BookingTravelBackend.Repository.PostRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.CategoryBlog;
import com.example.BookingTravelBackend.entity.CommentPost;
import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpls implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentPostRepository commentPostRepository;
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
    public Post findById(int id) {
        return postRepository.findById(id).get();
    }

    @Override
    @Transactional
    public PostResponse AddPost(PostRequest postRequest) {
        Post post= new Post();
        post.setDatePost(postRequest.getDatePost());
        post.setPostImg(postRequest.getPostImg());
        post.setPostTitle(postRequest.getPostTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(categoryRepository.findByCategoryName("%"+postRequest.getCategoryName()+"%"));
        User user = userRepository.findById(postRequest.getUserId()).get();
        post.setUserPost(user);
        return new PostResponse(postRepository.save(post));
    }

    @Override
    public CommentPostResponse CommentPost(CommentRequest request, User user) {
        System.out.println("PostId: "+request.getPostid());
        Post post = postRepository.findById(request.getPostid()).get();

        CommentPost comment = new CommentPost();
        comment.setComment(request.getComment());
        comment.setCommentPost(post);
        comment.setUser(user);
        CommentPostResponse response = new CommentPostResponse(commentPostRepository.save(comment));
        return response;
    }
}
