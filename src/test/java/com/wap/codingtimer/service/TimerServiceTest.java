package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.domain.Timer;
import com.wap.codingtimer.dto.CurrentTimerStatusDto;
import com.wap.codingtimer.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimerServiceTest {

    @Autowired
    TimerService timerService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 신규_유저의_타이머_상태() throws Exception {
        //given
        Member member = memberRepository.save(new Member());

        //when
        CurrentTimerStatusDto timerDto = timerService.getCurrentUserStatus(member.getId());

        //then
        System.out.println(timerDto);
    }

    @Test
    void 신규_유저의_타이머_시작() throws Exception {
        //given
        Member member = memberRepository.save(new Member());

        //when
        Timer timer = timerService.start(member.getId());

        //then
        System.out.println("timer = " + timer);
    }

}