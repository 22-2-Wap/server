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
}
