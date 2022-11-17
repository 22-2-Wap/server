package com.wap.codingtimer.timer.repository;

import com.wap.codingtimer.timer.domain.Timer;

import java.util.Optional;

public interface TimerCustomRepository {
    Optional<Timer> findRecentTimer(String memberId);
}
