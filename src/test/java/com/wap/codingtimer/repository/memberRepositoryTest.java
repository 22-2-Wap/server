package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class memberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원저장() throws Exception{
        //given
        Member member=new Member();

        //when
        memberRepository.save(member);

        //then
        Member findMember=memberRepository.findById(member.getId()).get();
        assertThat(findMember).isNotNull();
    }

    @Test
    public void 중복회원_검사() throws Exception{
        //given
        Member member1=new Member();

        //when
        member1.setNickname("재현");
        memberRepository.save(member1);

        //then
        assertThat(memberRepository.existsMemberByNickname("재현")).isTrue();
    }
}