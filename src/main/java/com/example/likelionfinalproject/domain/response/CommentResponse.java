package com.example.likelionfinalproject.domain.response;

import com.example.likelionfinalproject.domain.dto.CommentDto;
import com.example.likelionfinalproject.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;

    public static CommentResponse toCommentResponse(CommentDto comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUserName())
                .postId(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
