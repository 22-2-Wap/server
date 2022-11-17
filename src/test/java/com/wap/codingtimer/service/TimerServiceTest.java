package com.wap.codingtimer.service;

import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import com.wap.codingtimer.timer.TimerService;
import com.wap.codingtimer.timer.domain.Timer;
import com.wap.codingtimer.timer.dto.CurrentTimerStatusDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TimerServiceTest {

    @Autowired
    TimerService timerService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 신규_유저의_타이머_상태() throws Exception {
        //given
        Member member = memberRepository.save(new Member("id", "pw", "nickname"));

        //when
        CurrentTimerStatusDto timerDto = timerService.getCurrentUserStatus(member.getId());

        //then
        System.out.println(timerDto);
    }

    @Test
    void 신규_유저의_타이머_시작() throws Exception {
        //given
        Member member = memberRepository.save(new Member("id", "pw", "nickname"));

        //when
        Timer timer = timerService.start(member.getId());

        //then
        System.out.println("timer = " + timer);
    }
}