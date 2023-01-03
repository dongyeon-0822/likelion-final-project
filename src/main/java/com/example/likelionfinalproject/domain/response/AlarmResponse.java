package com.example.likelionfinalproject.domain.response;

import com.example.likelionfinalproject.domain.entity.Alarm;
import com.example.likelionfinalproject.domain.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private Long fromUserId;
    private Long targetId;
    private String text;
    private LocalDateTime createdAt;

    public static AlarmResponse toAlarmResponse(Alarm alarm) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getFromTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build();
    }
}
