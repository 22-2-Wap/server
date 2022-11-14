package com.wap.codingtimer.timer;

import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import com.wap.codingtimer.timer.domain.StudyingStatus;
import com.wap.codingtimer.timer.domain.Timer;
import com.wap.codingtimer.timer.dto.CurrentTimerStatusDto;
import com.wap.codingtimer.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
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
    private final int MAX_DIFF_MINUTES = 8 * 60;

    @Transactional
    public Timer getTimer(Long memberId) {
        Optional<Timer> optionalTimer = timerRepository.findRecentTimer(memberId);
        LocalDate today = getTodayDateTime();

        //해당 유저의 타이머가 없거나 지난 날의 타이머일 때
        if (optionalTimer.isEmpty() || optionalTimer.get().getDate().isBefore(today)) {
            Timer timer = new Timer(today);
            timer.setMember(memberRepository.findById(memberId).get());
            timerRepository.save(timer);

            return timer;
        }

        //오늘의 타이머가 있을 때
        return optionalTimer.get();
    }

    @Transactional
    public CurrentTimerStatusDto getCurrentUserStatus(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Timer timer = getTimer(memberId);

        return new CurrentTimerStatusDto(member.getNickname(),
                timer.getSumMinutes(),
                LocalDateTime.now(),
                timer.getStatus());
    }

    @Transactional
    public Timer start(Long memberId) {
        Timer timer = getTimer(memberId);
        LocalDateTime now = LocalDateTime.now();

        if (timer.getSumMinutes() == 0)
            timer.setFirstStartedTime(now);

        timer.setTimerStart(now);
        timer.setStatus(StudyingStatus.STUDY);

        return timer;
    }

    @Transactional
    public void stop(Long memberId) {
        LocalDate today = getTodayDateTime();
        LocalDateTime todayStartedTime = today.atTime(TIME_OF_THE_DAY_STARTED);
        LocalDateTime now = LocalDateTime.now();

        Timer timer = timerRepository.findRecentTimer(memberId).get();
        int difference = getTimeDifference(timer.getTimerStart(), now);

        timer.setStatus(StudyingStatus.REST);

        //최대 연속 공부 시간을 넘긴 경우
        if (difference > MAX_DIFF_MINUTES)
            return;

        //날짜가 같은 경우
        if (timer.getDate().isEqual(today)) {
            timer.setTimerStop(now);
            timer.setLastEndedTime(now);
            timer.addSumMinutes(difference);

            return;
        }

        //날짜가 지난 경우
        //기존 타이머 세팅
        difference = getTimeDifference(timer.getTimerStart(), now);
        timer.addSumMinutes(difference);
        timer.setTimerStop(todayStartedTime);
        timer.setLastEndedTime(todayStartedTime);

        //새 타이머 세팅
        difference = getTimeDifference(timer.getTimerStart(), now);
        timer = getTimer(memberId);
        timer.addSumMinutes(difference);
        timer.setFirstStartedTime(todayStartedTime);
        timer.setTimerStart(todayStartedTime);
        timer.setTimerStop(now);
        timer.setLastEndedTime(now);
    }

    private int getTimeDifference(LocalDateTime before, LocalDateTime after) {
        return (int) (Duration.between(before, after).getSeconds() / 60);
    }

    private LocalDate getTodayDateTime() {
        LocalDateTime today = LocalDateTime.now();

        if (today.getHour() > 6)
            today = LocalDateTime.of(today.toLocalDate(), TIME_OF_THE_DAY_STARTED);
        else
            today = LocalDateTime.of(today.minusDays(1L).toLocalDate(), TIME_OF_THE_DAY_STARTED);

        return today.toLocalDate();
    }
}
