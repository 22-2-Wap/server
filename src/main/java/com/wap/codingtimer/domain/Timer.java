package com.wap.codingtimer.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
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

    /**
     * 도메인 로직
     */
    public void setMember(Member member) {
        this.member = member;
    }

    public void setTimerStart(LocalDateTime timerStart) {
        this.timerStart = timerStart;
    }

    public void setTimerStop(LocalDateTime timerStop) {
        this.timerStop = timerStop;
    }

    public void setFirstStartedTime(LocalDateTime firstStartedTime) {
        this.firstStartedTime = firstStartedTime;
    }

    public void setLastEndedTime(LocalDateTime lastEndedTime) {
        this.lastEndedTime = lastEndedTime;
    }

    public void setStatus(StudyingStatus status) {
        this.status = status;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
