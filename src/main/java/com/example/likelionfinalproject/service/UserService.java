package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.response.UserJoinResponse;
import com.example.likelionfinalproject.domain.response.UserLoginResponse;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.UserRepository;
import com.example.likelionfinalproject.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000 * 60 * 60l;

    public UserJoinResponse join(String userName, String password) {
        // userName 중복 체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();
        User savedUser = userRepository.save(user);

        return new UserJoinResponse(savedUser.getUserId(), savedUser.getUserName());
    }
    public UserLoginResponse login(String userName, String password) {
        // userName 있는지 여부 확인, 없으면 NOT_FOUND 에러 발생
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s는 가입된 적이 없습니다.", userName)));

        // password 일치 하는지 여부 확인
        if(!encoder.matches(password, selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, String.format("password를 잘못 입력했습니다. "));
        }

        // 두가지 확인 중 예외 안났으면 Token 발행
        String token = JwtTokenUtil.createToken(selectedUser.getUserName(), key, expireTimeMs);
        return new UserLoginResponse(token);
    }
}
