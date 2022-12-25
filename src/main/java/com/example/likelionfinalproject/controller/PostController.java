package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.PostAddRequest;
import com.example.likelionfinalproject.domain.response.PostAddResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Response> addPost(Authentication authentication, @RequestBody PostAddRequest postAddRequest) {
        PostDto postDto = postService.addPost(authentication.getName(), postAddRequest);
        return ResponseEntity.ok().body(Response.success(new PostAddResponse("포스트 등록 완료", postDto.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPost(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok().body(Response.success(postDto));
    }
}
