package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.domain.StudyingStatus;
import com.wap.codingtimer.domain.Timer;
import com.wap.codingtimer.dto.CurrentTimerStatusDto;
import com.wap.codingtimer.repository.MemberRepository;
import com.wap.codingtimer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final MemberRepository memberRepository;

    private final LocalTime TIME_OF_THE_DAY_STARTED = LocalTime.of(6, 0);
    private final int MAX_DIFF_HOUR = 8;

    public CurrentTimerStatusDto getCurrentUserStatus(Long memberId) {
        Optional<Timer> optionalTimer = timerRepository.findTopByMemberIdOrderByFirstStartedTimeDesc(memberId);
        LocalDateTime now = LocalDateTime.now();

        if (optionalTimer.isEmpty())
            return new CurrentTimerStatusDto(0, now, StudyingStatus.REST);

        Timer timer = optionalTimer.get();

        if (timer.getStatus() == StudyingStatus.REST)
            return new CurrentTimerStatusDto(0, now, StudyingStatus.REST);

        Duration between = Duration.between(timer.getTimerStart(), now);
        int seconds = (int) between.getSeconds();

        return new CurrentTimerStatusDto(seconds, now, StudyingStatus.STUDY);
    }

    @Transactional
    public Timer start(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Optional<Timer> optionalTimer = timerRepository.findTopByMemberIdOrderByFirstStartedTimeDesc(memberId);

        //타이머가 하나도 없으면 타이머 생성
        if (optionalTimer.isEmpty())
            return timerRepository.save(createTimer(member));

        Timer userTimer = optionalTimer.get();
        LocalDateTime today = getTodayDateTime();

        //하루가 지났으면 타이머 생성
        if (today.isAfter(userTimer.getTimerStart()))
            return timerRepository.save(createTimer(member));

        //하루가 안 지났으면 기존 타이머 리턴
        userTimer.setStatus(StudyingStatus.STUDY);
        userTimer.setTimerStart(LocalDateTime.now());

        return userTimer;
    }

    @Transactional
    public void stop(Long memberId) {
        Timer userTimer = timerRepository.findTopByMemberIdOrderByFirstStartedTimeDesc(memberId).get();
        userTimer.setStatus(StudyingStatus.REST);

        LocalDateTime today = getTodayDateTime();
        LocalDateTime now = LocalDateTime.now();

        Duration between = Duration.between(userTimer.getTimerStart(), now);
        int diff = (int) between.getSeconds() / 60 / 60;

        //최대 시간을 초과했다면 공부 시간 인정 X
        if (diff > MAX_DIFF_HOUR)
            return;

        //하루가 안 지났으면 기존 타이머 사용
        if (userTimer.getTimerStart().isAfter(today)) {
            userTimer.setTimerStop(now);
            userTimer.setLastEndedTime(now);
            userTimer.setSum(userTimer.getSum() + diff);

            return;
        }

        /**
         *        하루가 지났으면 새 타이머 사용
         */
        //이전 타이머 오늘 오전 6시로 마무리
        between = Duration.between(userTimer.getTimerStart(), today);
        diff = (int) between.getSeconds() / 60 / 60;

        userTimer.setTimerStop(today);
        userTimer.setLastEndedTime(today);
        userTimer.setSum(diff);

        //새 타이머 현재 시각으로 마무리
        between = Duration.between(today, now);
        diff = (int) between.getSeconds() / 60 / 60;

        Timer newTimer = new Timer();
        newTimer.setMember(memberRepository.findById(memberId).get());
        newTimer.setTimerStart(today);
        newTimer.setFirstStartedTime(today);
        newTimer.setTimerStop(now);
        newTimer.setLastEndedTime(now);
        newTimer.setStatus(StudyingStatus.REST);
        newTimer.setSum(diff);

        timerRepository.save(newTimer);
    }

    private LocalDateTime getTodayDateTime() {
        LocalDateTime today = LocalDateTime.now();

        if (today.getHour() > 6)
            today = LocalDateTime.of(today.toLocalDate(), TIME_OF_THE_DAY_STARTED);
        else
            today = LocalDateTime.of(today.minusDays(1L).toLocalDate(), TIME_OF_THE_DAY_STARTED);

        return today;
    }

    private Timer createTimer(Member member) {
        Timer timer = new Timer();
        timer.setMember(member);

        LocalDateTime now = LocalDateTime.now();
        timer.setFirstStartedTime(now);
        timer.setTimerStart(now);
        timer.setStatus(StudyingStatus.STUDY);

        return timer;
    }

}
