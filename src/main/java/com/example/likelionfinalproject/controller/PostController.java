package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.domain.response.PostResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Response> addPost(Authentication authentication, @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.addPost(authentication.getName(), postRequest);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 등록 완료", postDto.getId())));
    }

    @GetMapping("")
    public ResponseEntity<Response> getPostList(Pageable pageable) {
        Page<PostDto> postDtoPage = postService.getPostList(pageable);
        return ResponseEntity.ok().body(Response.success(postDtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPost(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok().body(Response.success(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> editPost(Authentication authentication, @PathVariable Long id, @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.editPost(authentication.getName(), id, postRequest);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 수정 완료", postDto.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(Authentication authentication, @PathVariable Long id) {
        Long deletedPostId = postService.deletePost(authentication.getName(), id);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 삭제 완료", deletedPostId)));
    }
}
