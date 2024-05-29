package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.PostRepository;
import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;
import com.example.BookingTravelBackend.service.PostService;
import com.example.BookingTravelBackend.util.HandleSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpls implements PostService {
    private final PostRepository postRepository;

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
}
