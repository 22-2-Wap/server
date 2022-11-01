package com.wap.codingtimer.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Timer {

    @Id
    @GeneratedValue
    @Column(name = "timer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime timerStart;
    private LocalDateTime timerStop;

    private LocalDateTime firstStartedTime;
    private LocalDateTime lastEndedTime;

    private StudyingStatus status;

    private int sum;

    /*
      비즈니스 로직
     */
    public void start() {
        this.timerStart = LocalDateTime.now();
        this.status = StudyingStatus.STUDY;
    }

    public void stop() {
        this.timerStop = LocalDateTime.now();
        this.status = StudyingStatus.REST;
    }
}
