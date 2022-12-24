package com.example.likelionfinalproject.domain.dto;

import com.example.likelionfinalproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private String body;
    private Long userId;

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .body(post.getBody())
                .userId(post.getUser().getUserId())
                .build();
    }
}
