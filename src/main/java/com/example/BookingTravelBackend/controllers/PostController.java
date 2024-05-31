package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.Request.PostRequest;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.PaginationResponse;
import com.example.BookingTravelBackend.payload.respone.PostResponse;
import com.example.BookingTravelBackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping ("/getPost")
    public ResponseEntity<HttpRespone> getPostByNum (@RequestParam(value = "search", defaultValue = "") String search,
                                        @RequestParam ("pageNum")int pageNum){
        PaginationResponse paginationResponse = postService.findAllPost(search,pageNum,6);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(),"success",paginationResponse));
    }

    @GetMapping ("/getPostById")
    public ResponseEntity<HttpRespone> getPostById (@RequestParam ("id") int id){
        PostResponse response = new PostResponse(postService.findById(id));
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }
    @PostMapping("/addPost")
    public ResponseEntity<HttpRespone> addPost (@RequestBody PostRequest postRequest){
        PostResponse response = postService.AddPost(postRequest);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }
}
