package com.wap.codingtimer.auth;

import com.wap.codingtimer.auth.domain.JwtTokenProvider;
import com.wap.codingtimer.auth.domain.SocialLoginType;
import com.wap.codingtimer.member.MemberService;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class OauthController {
//    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping
    public String login(@RequestBody Map<String, Object> request) {
        log.info("user id = {}", request.get("id"));
        Member user = memberRepository.findById((String) request.get("id"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        if(request.get("pw").equals(user.getPassword()))
            return jwtTokenProvider.createToken(user.getId(), user.getNickname());

        return "fail";
    }

    @PostMapping("join")
    public String join(@RequestBody Map<String, Object> member) {
        String snsId = (String) member.get("user");
        String password = (String) member.get("password");

//        memberRepository.save(Member.test(snsId, password));
        return snsId;
    }

//    @GetMapping("{socialLoginType}")
//    public void socialLoginType(@PathVariable("socialLoginType") SocialLoginType socialLoginType) throws IOException {
//        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
//        oauthService.request(socialLoginType);
//    }
//
//    @GetMapping("{socialLoginType}/callback")
//    public String callback(@PathVariable("socialLoginType") SocialLoginType socialLoginType,
//                           @RequestParam("code") String code) {
//        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
//        return oauthService.requestAccessToken(socialLoginType, code);
//    }
}
