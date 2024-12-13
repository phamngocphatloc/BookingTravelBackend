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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
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
    private final ReportRepository reportRepository;
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

        // Lấy danh sách tất cả các postId của các bài viết trong trang hiện tại
        List<Integer> postIds = pagePost.getContent().stream()
                .map(Post::getPostId)
                .collect(Collectors.toList());

        // Truy vấn lấy tất cả trạng thái của các bài viết một lần
        List<Tuple> statusTuples = postRepository.findPostStatusesByPostIds(postIds);

        // Tạo map để tra cứu nhanh trạng thái của từng bài viết
        Map<Integer, PostStatusResponse> postStatusMap = statusTuples.stream()
                .collect(Collectors.toMap(
                        tuple -> (Integer) tuple.get("post_id"),  // Lấy postId từ Tuple
                        tuple -> new PostStatusResponse(
                                (int) tuple.get("total_likes"),
                                (int) tuple.get("total_dislikes"),
                                (int) tuple.get("total_comments")
                        )
                ));

        // Tạo danh sách PostResponse
        List<PostResponse> listPostResponse = new ArrayList<>(pageSize);

        // Duyệt qua các bài viết và gắn trạng thái vào PostResponse
        for (Post item : pagePost.getContent()) {
            // Lấy trạng thái của bài viết từ map
            PostStatusResponse status = postStatusMap.get(item.getPostId());

            // Tạo đối tượng PostResponse và gắn trạng thái
            PostResponse response = new PostResponse(item);
            response.setPostStatusResponse(status);

            // Thêm vào danh sách kết quả
            listPostResponse.add(response);
        }

        // Trả về kết quả phân trang
        return new PaginationResponse(pageNum, pageSize, pagePost.getTotalElements(), pagePost.isLast(),pagePost.getTotalPages(),listPostResponse);
    }


    @Override
    public Post findById(int id) {
        return postRepository.findById(id).get();
    }

    @Override
    @Transactional
    public PostResponse AddPost(PostRequest postRequest) {
        if (postRequest.getContent().equalsIgnoreCase("")) {
            throw new IllegalArgumentException("Vui Lòng Nhập Nội Dung Bài Viết");
        }
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
        if (request.getComment().equalsIgnoreCase("")){
            throw new IllegalArgumentException("Vui Lòng Nhập Comment");
        }
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
        if (post.isDelete()){
            throw new IllegalArgumentException("Bài Viết Đã Bị Xoá");
        }
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

    @Override
    public HttpRespone GetAllPost() {
        List<Post> listposts = postRepository.findAll();
        List<PostResponse> response = new ArrayList<>();
        if (!listposts.isEmpty()) {
            listposts.stream().forEach(item -> {
                response.add(new PostResponse(item));
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", response);
    }

    @Override
    @Transactional
    public void DeletePost(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
           throw new IllegalArgumentException("Không Tìm Thấy Bài Viết Này");
        });
        post.setDelete(true);
        postRepository.save(post);
    }

    @Override
    public HttpRespone report(int postId) {
        Report report = new Report();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Tìm Thấy Bài Viết");
        });

        report.setReportType("POST");
        report.setReportedAt(new Timestamp(System.currentTimeMillis()));
        report.setPost(post);
        report.setReportedBy(principal);
        report.setDescription("Bao Cao Bai Viet");
        report.setStatus("pending");
        report.setReportedUser(post.getUserPost());
        Report reportSaved = reportRepository.save(report);
        return new HttpRespone(HttpStatus.OK.value(), "success", new ReportRespone(reportSaved));
    }


}
