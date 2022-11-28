package com.wap.codingtimer.auth.service;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if(!member.getSocialLoginType().equals(SocialLoginType.LOCAL))
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        return member;
    }
}
