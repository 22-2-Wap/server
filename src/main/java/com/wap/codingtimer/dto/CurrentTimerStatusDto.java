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

    private final int sec;
    private final LocalDateTime serverTime;
    private final StudyingStatus status;
}
