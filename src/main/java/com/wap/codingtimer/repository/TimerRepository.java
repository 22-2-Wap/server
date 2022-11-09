package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    Optional<Timer> findTopByMemberIdOrderByFirstStartedTimeDesc(Long memberId);
}
