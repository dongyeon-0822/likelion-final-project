package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.dto.CommentDto;
import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.Comment;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.CommentRequest;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.CommentRepository;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentDto addComment(String userName, Long postId, CommentRequest commentRequest) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.save(Comment.of(commentRequest.getComment(),user,post));
        return CommentDto.toCommentDto(comment);
    }
}
