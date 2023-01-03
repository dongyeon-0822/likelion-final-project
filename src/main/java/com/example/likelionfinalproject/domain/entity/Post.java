package com.example.likelionfinalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private boolean isDeleted = Boolean.FALSE;

    public static Post of(String title, String body, User user) {
        Post post = Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
        return post;
    }
}
