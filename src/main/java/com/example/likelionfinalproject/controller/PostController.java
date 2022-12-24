package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.domain.response.PostResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Response> addPost(Authentication authentication, @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.addPost(authentication.getName(), postRequest);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 등록 완료", postDto.getPostId())));
    }
}
