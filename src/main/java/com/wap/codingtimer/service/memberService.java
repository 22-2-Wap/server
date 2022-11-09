package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class memberService {

    private final MemberRepository memberRepository;

    public boolean isDuplicate(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    @Transactional
    public Long changeNickname(Long memberId, String nickname){
        Member member = memberRepository.findById(memberId).get();
        member.setNickname(nickname);

        return memberId;
    }

    
}
