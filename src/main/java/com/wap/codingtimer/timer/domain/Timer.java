package com.wap.codingtimer.timer.domain;

import com.wap.codingtimer.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
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
    private int sumMinutes;

    private LocalDate date;

    /**
     * 도메인 로직
     */
    public Timer(LocalDate date) {
        this.date = date;
        status = StudyingStatus.REST;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTimerStart(LocalDateTime timerStart) {
        this.timerStart = timerStart;
    }

    public void setTimerStop(LocalDateTime timerStop) {
        this.timerStop = timerStop;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void addSumMinutes(int sum) {
        this.sumMinutes += sum;
    }
}
