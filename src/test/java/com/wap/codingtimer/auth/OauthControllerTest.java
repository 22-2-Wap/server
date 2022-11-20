package com.wap.codingtimer.auth;

import com.wap.codingtimer.auth.dto.LoginDto;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OauthControllerTest {

    @Autowired PasswordEncoder bCryptPasswordEncoder;
    @Autowired OauthController oauthController;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 비밀번호_비교_테스트() throws Exception{
        //given
        String pw = "test";
        String encode = bCryptPasswordEncoder.encode(pw);

        //when
        bCryptPasswordEncoder.matches(pw, encode);

        //then
        assertThat(bCryptPasswordEncoder.matches(pw, encode)).isTrue();
    }

    @Test
    void 저장_후_비밀번호_비교() throws Exception{
        //given
        LoginDto loginDto = new LoginDto();
        loginDto.setId("test");
        loginDto.setPw("test");

        //when
        oauthController.join(loginDto);
        Member member = memberRepository.findById(loginDto.getId()).get();

        //then
//        assertThat(oauthController.login(loginDto)).isNotNull();
        System.out.println(bCryptPasswordEncoder.encode(loginDto.getPw()));
        System.out.println(member.getPassword());
        assertThat(bCryptPasswordEncoder.matches(loginDto.getPw(), member.getPassword())).isTrue();
//        assertThat(bCryptPasswordEncoder.matches(loginDto.getPw(), bCryptPasswordEncoder.encode(loginDto.getPw()))).isTrue();
    }

}