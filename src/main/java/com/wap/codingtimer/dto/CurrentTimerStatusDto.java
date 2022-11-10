package com.wap.codingtimer.dto;

import com.wap.codingtimer.domain.StudyingStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@RequiredArgsConstructor
public class CurrentTimerStatusDto {
    private final String nickname;
    private final int minutes;
    private final LocalDateTime serverTime;
    private final StudyingStatus status;
}
