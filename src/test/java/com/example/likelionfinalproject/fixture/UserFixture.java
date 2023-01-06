package com.example.likelionfinalproject.fixture;

import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserFixture {
    public static User get(String userName, String password){
        return User.builder()
                .userId(1l)
                .userName(userName)
                .password(password)
                .build();
    }
}