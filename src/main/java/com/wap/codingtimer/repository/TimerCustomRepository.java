package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Timer;

import java.util.Optional;

public interface TimerCustomRepository {
    Optional<Timer> findRecentTimer(Long memberId);
}
