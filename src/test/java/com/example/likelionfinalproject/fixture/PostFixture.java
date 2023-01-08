package com.example.likelionfinalproject.fixture;

import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class PostFixture {
    public static Post get(String userName, String password){
        return Post.builder()
                .id(1l)
                .title("title")
                .body("body")
                .user(UserFixture.get(userName, password))
                .build();
    }
}