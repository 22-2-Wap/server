package com.wap.codingtimer.timer.repository;

import com.wap.codingtimer.timer.domain.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long>, TimerCustomRepository {
}
