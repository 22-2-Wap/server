package com.wap.codingtimer.member.dto;

import com.wap.codingtimer.timer.domain.StudyingStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Getter
public class FriendsInfo {
    private final int index;
    private final String nickname;
    private final int todayStudyingMinutes;
    private final StudyingStatus status;
}
