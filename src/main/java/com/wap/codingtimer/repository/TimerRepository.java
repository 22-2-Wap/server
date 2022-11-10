package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long>, TimerCustomRepository {
}
