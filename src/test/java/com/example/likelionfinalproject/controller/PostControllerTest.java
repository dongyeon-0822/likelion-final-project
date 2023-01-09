package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.CommentDto;
import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.CommentRequest;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.service.CommentService;
import com.example.likelionfinalproject.service.LikeService;
import com.example.likelionfinalproject.service.PostService;
import com.example.likelionfinalproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;
    @MockBean
    CommentService commentService;
    @MockBean
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포스트 등록 성공")
    @WithMockUser
    void addPost() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        PostDto postDto = PostDto.builder()
                .id(0l)
                .build();

        when(postService.addPost(any(),any()))
                .thenReturn(postDto);

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(postRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.message").exists())
            .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("포스트 등록 실패 - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void addPost_fail1() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        when(postService.addPost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void getPost() throws Exception {
        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("title")
                .body("body")
                .userName("user")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();

        when(postService.getPost(any()))
                .thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());

    }

    @Test
    @DisplayName("포스트 리스트 조회 성공")
    @WithMockUser
    void getPostList() throws Exception {

        mockMvc.perform(get("/api/v1/posts")
                        .param("pageNumber", "0")
                        .param("pageSize", "20")
                        .param("sort", "id,desc"))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(postService).getPostList(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(0, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertEquals(Sort.by("id","desc"), pageable.withSort(Sort.by("id", "desc")).getSort());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void editPost() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("title")
                .body("body")
                .userName("user")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();


        when(postService.editPost(any(),any(),any()))
                .thenReturn(postDto);

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());

    }

    @Test
    @DisplayName("포스트 수정 실패 1 - 인증 실패")
    @WithAnonymousUser
    void editPost_fail1() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        when(postService.editPost(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
    @Test
    @DisplayName("포스트 수정 실패 2 - 작성자 불일치")
    @WithMockUser
    void editPost_fail2() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        when(postService.editPost(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));

    }

    @Test
    @DisplayName("포스트 수정 실패 3 - DB 에러")
    @WithMockUser
    void editPost_fail3() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        when(postService.editPost(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));


        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));

    }

    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void deletePost() throws Exception {
        Long postId = 1l;

        when(postService.deletePost(any(),any()))
                .thenReturn(postId);

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());

    }
    @Test
    @DisplayName("포스트 삭제 실패 1 - 인증 실패")
    @WithAnonymousUser
    void deletePost_fail1() throws Exception {
        when(postService.deletePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
    @Test
    @DisplayName("포스트 삭제 실패 2 - 작성자 불일치")
    @WithMockUser
    void deletePost_fail2() throws Exception {
        when(postService.deletePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));

    }

    @Test
    @DisplayName("포스트 삭제 실패 3 - DB 에러")
    @WithMockUser
    void deletePost_fail3() throws Exception {
        when(postService.deletePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));

    }

    @Test
    @DisplayName("댓글 작성 성공")
    @WithMockUser
    void addComment_success() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .comment(commentRequest.getComment())
                .postId(1l)
                .build();

        when(commentService.addComment(any(),any(),any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("댓글 작성 실패 1 - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void addComment_fail1() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment")
                .build();

        when(commentService.addComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 작성 실패 2 - 게시물 존재 X")
    @WithMockUser
    void addComment_fail2() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment")
                .build();

        when(commentService.addComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("댓글리스트 조회 성공")
    @WithMockUser
    void getCommentList_success() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .comment("comment")
                .postId(1l)
                .build();
        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(commentDto));

        when(commentService.getCommentList(any(),any()))
                .thenReturn(commentDtoPage);

        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].comment").exists())
                .andExpect(jsonPath("$.result.content[0].postId").exists());
    }

    @Test
    @DisplayName("댓글 조회 성공")
    @WithMockUser
    void getComment_success() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .comment("comment")
                .postId(1l)
                .build();

        when(commentService.getComment(any(),any()))
                .thenReturn(commentDto);

        mockMvc.perform(get("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void editComment_success() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .comment(commentRequest.getComment())
                .postId(1l)
                .build();

        when(commentService.editComment(any(),any(),any(),any()))
                .thenReturn(commentDto);

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.comment").value(commentRequest.getComment()))
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("댓글 수정 실패 1 - 인증 실패")
    @WithAnonymousUser
    void editComment_fail1() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();

        when(commentService.editComment(any(),any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 수정 실패 2 - 게시물 존재 X")
    @WithMockUser
    void editComment_fail2() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();

        when(commentService.editComment(any(),any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));;
    }

    @Test
    @DisplayName("댓글 수정 실패 3 - 작성자 불일치")
    @WithMockUser
    void editComment_fail3() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();

        when(commentService.editComment(any(),any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()));;
    }

    @Test
    @DisplayName("댓글 수정 실패 4 - DB 에러")
    @WithMockUser
    void editComment_fail4() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();

        when(commentService.editComment(any(),any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DATABASE_ERROR.name()));;
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void deleteComment_success() throws Exception {
        Long deletedCommentId = 1l;

        when(commentService.deleteComment(any(),any(),any()))
                .thenReturn(deletedCommentId);

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("댓글 삭제 완료"))
                .andExpect(jsonPath("$.result.id").value(deletedCommentId));
    }

    @Test
    @DisplayName("댓글 삭제 실패 1 - 인증 실패")
    @WithAnonymousUser
    void deleteComment_fail1() throws Exception {
        when(commentService.deleteComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 삭제 실패 2 - 게시물 존재 X")
    @WithMockUser
    void deleteComment_fail2() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .comment("edit comment")
                .build();

        when(commentService.deleteComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 3 - 작성자 불일치")
    @WithMockUser
    void deleteComment_fail3() throws Exception {
        when(commentService.deleteComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()));;
    }

    @Test
    @DisplayName("댓글 삭제 실패 4 - DB 에러")
    @WithMockUser
    void deleteComment_fail4() throws Exception {
        when(commentService.deleteComment(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DATABASE_ERROR.name()));;
    }

    @Test
    @DisplayName("좋아요 누르기 성공")
    @WithMockUser
    void likePost_success() throws Exception {
        when(likeService.likePost(any(),any()))
                .thenReturn(1l);

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다"));
    }

    @Test
    @DisplayName("좋아요 누르기 실패 1 - 로그인하지 않은 경우")
    @WithAnonymousUser
    void likePost_fail1() throws Exception {
        when(likeService.likePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("좋아요 누르기 실패 2 - 게시물 존재 X")
    @WithMockUser
    void likePost_fail2() throws Exception {
        when(likeService.likePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("좋아요 누르기 실패 3 - 좋아요 중복")
    @WithMockUser
    void likePost_fail3() throws Exception {
        when(likeService.likePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_LIKES, ErrorCode.DUPLICATED_LIKES.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DUPLICATED_LIKES.name()));
    }

    @Test
    @DisplayName("좋아요 개수조회 성공")
    @WithMockUser
    void countLikes_success() throws Exception {
        when(likeService.countLikes(any()))
                .thenReturn(0l);

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(0));
    }

    @Test
    @DisplayName("좋아요 개수조회 실패 - 게시물 존재 X")
    @WithMockUser
    void countLikes_fail() throws Exception {
        when(likeService.countLikes(any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    @WithMockUser
    void unlikePost_success() throws Exception {
        when(likeService.unlikePost(any(),any()))
                .thenReturn(1l);

        mockMvc.perform(delete("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("좋아요를 취소했습니다"));
    }

    @Test
    @DisplayName("좋아요 취소 실패 1 - 로그인하지 않은 경우")
    @WithAnonymousUser
    void unlikePost_fail1() throws Exception {
        when(likeService.unlikePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("좋아요 취소 실패 2 - 게시물 존재 X")
    @WithMockUser
    void unlikePost_fail2() throws Exception {
        when(likeService.unlikePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("좋아요 취소 실패 3 - 좋아요 취소 중복")
    @WithMockUser
    void unlikePost_fail3() throws Exception {
        when(likeService.unlikePost(any(),any()))
                .thenThrow(new AppException(ErrorCode.LIKES_NOT_FOUND, ErrorCode.LIKES_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.LIKES_NOT_FOUND.name()));
    }
}