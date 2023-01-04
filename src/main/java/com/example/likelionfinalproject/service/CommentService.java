package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.dto.CommentDto;
import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.*;
import com.example.likelionfinalproject.domain.request.CommentRequest;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.AlarmRepository;
import com.example.likelionfinalproject.repository.CommentRepository;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    public CommentDto addComment(String userName, Long postId, CommentRequest commentRequest) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.save(Comment.of(commentRequest.getComment(),user,post));

        alarmRepository.save(Alarm.of(AlarmType.NEW_COMMENT_ON_POST, post.getUser(), user.getUserId(), postId, AlarmType.NEW_COMMENT_ON_POST.getAlarmText()));
        return CommentDto.toCommentDto(comment);
    }

    public CommentDto getComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));
        return CommentDto.toCommentDto(comment);
    }

    public Page<CommentDto> getCommentList(Pageable pageable, Long postId) {
        Page<Comment> commentPage = commentRepository.findAllByPostId(postId, pageable);
        Page<CommentDto> commentDtoPage = commentPage.map(comment -> CommentDto.toCommentDto(comment));
        return commentDtoPage;
    }

    public CommentDto editComment(String userName, Long postId, Long commentId, CommentRequest commentRequest) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));
        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        comment.setComment(commentRequest.getComment());
        Comment editedComment = commentRepository.save(comment);
        return CommentDto.toCommentDto(editedComment);
    }

    public Long deleteComment(String userName, Long postId, Long commentId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));
        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
        commentRepository.deleteById(commentId);
        return commentId;
    }
}
