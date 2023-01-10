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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    // Post CRUD
    @PostMapping("")
    @ApiOperation(value = "게시물 작성", notes = "로그인한 사용자만 게시물 작성 가능")
    public ResponseEntity<Response> addPost(Authentication authentication, @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.addPost(authentication.getName(), postRequest);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 등록 완료", postDto.getId())));
    }

    @GetMapping("")
    @ApiOperation(value = "전체 게시물 리스트 조회", notes = "한 페이지에 20개의 게시물 조회")
    public ResponseEntity<Response> getPostList(Pageable pageable) {
        Page<PostDto> postDtoPage = postService.getPostList(pageable);
        return ResponseEntity.ok().body(Response.success(postDtoPage));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "단일 게시물 조회", notes = "로그인 유무와 관계없이 파라미터로 넘기는 postId의 게시물 조회 가능")
    @ApiImplicitParam(name = "id", value = "게시물 ID")
    public ResponseEntity<Response> getPost(@PathVariable Long id) {
        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok().body(Response.success(postDto));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "게시물 수정", notes = "로그인한 사용자와 게시물 작성자가 동일할 때 수정 가능")
    @ApiImplicitParam(name = "id", value = "게시물 ID")
    public ResponseEntity<Response> editPost(Authentication authentication, @PathVariable Long id, @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.editPost(authentication.getName(), id, postRequest);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 수정 완료", postDto.getId())));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "게시물 삭제", notes = "로그인한 사용자와 게시물 작성자가 동일할 때 삭제 가능")
    @ApiImplicitParam(name = "id", value = "게시물 ID")
    public ResponseEntity<Response> deletePost(Authentication authentication, @PathVariable Long id) {
        Long deletedPostId = postService.deletePost(authentication.getName(), id);
        return ResponseEntity.ok().body(Response.success(new PostResponse("포스트 삭제 완료", deletedPostId)));
    }

    // Comment CRUD
    @PostMapping("/{postId}/comments")
    @ApiOperation(value = "댓글 작성", notes = "로그인한 사용자만 특정 게시물에 댓글 작성 가능")
    @ApiImplicitParam(name = "postId", value = "게시물 ID")
    public ResponseEntity<Response> addComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        CommentDto commentDto = commentService.addComment(authentication.getName(), postId, commentRequest);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @GetMapping("/{postId}/comments")
    @ApiOperation(value = "댓글리스트 조회", notes = "로그인 유무에 관계없이 특정 게시물에 달린 모든 댓글들을 한 페이지에 10개씩 조회 가능")
    @ApiImplicitParam(name = "postId", value = "게시물 ID")
    public ResponseEntity<Response> getCommentList(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long postId) {
        Page<CommentDto> CommentDtoPage = commentService.getCommentList(pageable, postId);
        return ResponseEntity.ok().body(Response.success(CommentDtoPage));
    }

    @GetMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value = "댓글 조회", notes = "로그인 유무에 관계없이 특정 게시물에 달린 특정 댓글 조회 가능")
    public ResponseEntity<Response> getComment(@Parameter(description = "게시물 ID") @PathVariable Long postId, @Parameter(description = "댓글 Id") @PathVariable Long commentId) {
        CommentDto commentDto = commentService.getComment(postId, commentId);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "로그인한 사용자와 댓글 작성자가 동일할 때 수정 가능")
    public ResponseEntity<Response> editComment(Authentication authentication, @Parameter(description = "게시물 ID") @PathVariable Long postId, @Parameter(description = "댓글 Id") @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentDto commentDto = commentService.editComment(authentication.getName(), postId, commentId, commentRequest);
        return ResponseEntity.ok().body(Response.success(CommentResponse.toCommentResponse(commentDto)));
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    @ApiOperation(value = "댓글 삭제", notes = "로그인한 사용자와 댓글 작성자가 동일할 때 삭제 가능")
    public ResponseEntity<Response> deleteComment(Authentication authentication, @Parameter(description = "게시물 ID") @PathVariable Long postId, @Parameter(description = "댓글 Id") @PathVariable Long commentId) {
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
