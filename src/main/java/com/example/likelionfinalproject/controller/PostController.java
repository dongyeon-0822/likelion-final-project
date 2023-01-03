package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.CommentDto;
import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.CommentRequest;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.domain.response.CommentDeleteResponse;
import com.example.likelionfinalproject.domain.response.CommentResponse;
import com.example.likelionfinalproject.domain.response.PostResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.CommentService;
import com.example.likelionfinalproject.service.LikeService;
import com.example.likelionfinalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    // Post CRUD
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

    // Comment CRUD
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Response> addComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        CommentDto commentDto = commentService.addComment(authentication.getName(), postId, commentRequest);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Response> getCommentList(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long postId) {
        Page<CommentDto> CommentDtoPage = commentService.getCommentList(pageable, postId);
        return ResponseEntity.ok().body(Response.success(CommentDtoPage));
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentDto commentDto = commentService.getComment(postId, commentId);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> editComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentDto commentDto = commentService.editComment(authentication.getName(), postId, commentId, commentRequest);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<Response> deleteComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId) {
        Long deletedCommentId = commentService.deleteComment(authentication.getName(), postId, commentId);
        return ResponseEntity.ok().body(Response.success(new CommentDeleteResponse("댓글 삭제 완료", deletedCommentId)));
    }

    // Likes
    @PostMapping("{postId}/likes")
    public ResponseEntity<Response> likePost(Authentication authentication, @PathVariable Long postId) {
        likeService.likePost(authentication.getName(), postId);
        return ResponseEntity.ok().body(Response.success("좋아요를 눌렀습니다"));
    }

    @GetMapping("{postId}/likes")
    public ResponseEntity<Response> countLikes(@PathVariable Long postId) {
        Long likes = likeService.countLikes(postId);
        return ResponseEntity.ok().body(Response.success(likes));
    }

    @DeleteMapping("{postId}/likes")
    public ResponseEntity<Response> unlikePost(Authentication authentication, @PathVariable Long postId) {
        likeService.unlikePost(authentication.getName(), postId);
        return ResponseEntity.ok().body(Response.success("좋아요를 취소했습니다"));
    }

    // My feed
    @GetMapping("/my")
    public ResponseEntity<Response> getMyFeed(Authentication authentication, Pageable pageable) {
        Page<PostDto> postDtoPage = postService.getMyFeed(pageable, authentication.getName());
        return ResponseEntity.ok().body(Response.success(postDtoPage));
    }
}
