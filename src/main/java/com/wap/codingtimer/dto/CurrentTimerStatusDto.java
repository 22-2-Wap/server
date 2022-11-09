package com.wap.codingtimer.dto;

import com.wap.codingtimer.domain.StudyingStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CurrentTimerStatusDto {

    private final int sec;
    private final LocalDateTime serverTime;
    private final StudyingStatus status;
}
