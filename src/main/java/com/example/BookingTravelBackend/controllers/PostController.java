package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.CommentRequest;
import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.CommentPostResponse;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;
import com.example.BookingTravelBackend.service.PostService;
import com.example.BookingTravelBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    @GetMapping ("/getPost")
    public ResponseEntity<HttpRespone> getPostByNum (@RequestParam(value = "search", defaultValue = "") String search,
                                        @RequestParam ("pageNum")int pageNum){
        PaginationResponse paginationResponse = postService.findAllPost(search,pageNum,6);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",paginationResponse));
    }

    @GetMapping ("/find_all_post")
    public ResponseEntity<HttpRespone> GetAllPost (@RequestParam ("pageNum")int pageNum){
        PaginationResponse paginationResponse = postService.findTrending(pageNum,6);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",paginationResponse));
    }

    @GetMapping ("/getPostById")
    public ResponseEntity<HttpRespone> getPostById (@RequestParam ("id") int id){
        PostResponse response = postService.findPostResponseById(id);

        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }

    @PostMapping("/comment")
    public ResponseEntity<HttpRespone> commentPost (@RequestBody CommentRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        CommentPostResponse response = postService.CommentPost(request,userLogin);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",response));
    }
    @PostMapping ("/addpost")
    public ResponseEntity<HttpRespone> addPost (@RequestBody PostRequest request){
        PostResponse response = postService.AddPost(request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",response));
    }

    @PutMapping ("/update_view")
    public ResponseEntity<HttpRespone> addPost (@RequestParam int id){
        int response = postService.updatePostById(id);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",response));
    }
}
