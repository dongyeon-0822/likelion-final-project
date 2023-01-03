package com.example.likelionfinalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE alarm SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Alarm extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AlarmType alarmType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private Long fromUserId;
    private Long fromTargetId;

    private String text;
    private boolean isDeleted = Boolean.FALSE;

    public static Alarm of(AlarmType alarmType, User user, Long fromUserId, Long fromTargetId, String text) {
        return Alarm.builder()
                .alarmType(alarmType)
                .user(user)
                .fromUserId(fromUserId)
                .fromTargetId(fromTargetId)
                .text(text)
                .build();
    }
}
