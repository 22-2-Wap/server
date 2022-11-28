package com.wap.codingtimer.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wap.codingtimer.auth.domain.JwtTokenProvider;
import com.wap.codingtimer.auth.domain.SocialLoginType;
import com.wap.codingtimer.auth.dto.LoginDto;
import com.wap.codingtimer.auth.dto.TokenDto;
import com.wap.codingtimer.auth.service.OauthService;
import com.wap.codingtimer.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OauthController {
    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String nickname = memberService.getNickname(loginDto.getId());
        TokenDto token = jwtTokenProvider.generateToken(authentication, nickname);

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        token.getRefreshToken(),
                        token.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return token;
    }

    @PostMapping("refresh")
    public TokenDto refresh(@RequestBody TokenDto tokenDto) {
        if (!jwtTokenProvider.validateToken(tokenDto.getRefreshToken()))
            return null;

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
        String refreshToken = (String) redisTemplate.opsForValue()
                .get("RT:" + authentication.getName());
        if (!refreshToken.equals(tokenDto.getRefreshToken()))
            return null;

        TokenDto newToken = jwtTokenProvider.generateToken(authentication,
                memberService.getNickname(authentication.getName()));

        redisTemplate.opsForValue()
                .set("RT:" +
                                authentication.getName(),
                        newToken.getRefreshToken(),
                        newToken.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return newToken;
    }

    @PostMapping("join")
    public String join(@RequestBody LoginDto loginDto) {
        return memberService.register(loginDto.getId(), loginDto.getPw(), loginDto.getNickname());
    }

    @GetMapping("validate/{nickname}")
    public String validateNickname(@PathVariable("nickname") String nickname) {
        boolean duplicate = memberService.isDuplicated(nickname);

        return duplicate? "": "OK";
    }

    @GetMapping("{socialLoginType}")
    public void socialLoginType(@PathVariable("socialLoginType") SocialLoginType socialLoginType) throws IOException {
        oauthService.request(socialLoginType);
    }

    @GetMapping("{socialLoginType}/callback")
    public TokenDto callback(@PathVariable("socialLoginType") SocialLoginType socialLoginType,
                           @RequestParam("code") String code) throws JsonProcessingException {

        String token = oauthService.requestAccessToken(socialLoginType, code);
        String email = oauthService.requestUserInfo(socialLoginType, token);

        if(memberService.isMemberExist(email))
            return snsLogin(email);

        String nickname = "임시유저_" + UUID.randomUUID();
        while (memberService.isDuplicated(nickname))
            nickname = "임시유저_" + UUID.randomUUID();

        memberService.register(email, socialLoginType, nickname);

        return snsLogin(email);
    }

    private TokenDto snsLogin(String email) {
        String nickname = memberService.getNickname(email);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        UserDetails principal = new User(email, "", List.of(authority));
        Authentication authenticate = new UsernamePasswordAuthenticationToken(principal, "", List.of(authority));

        TokenDto token = jwtTokenProvider.generateToken(authenticate, nickname);
        redisTemplate.opsForValue()
                .set("RT:" + email,
                        token.getRefreshToken(),
                        token.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return token;
    }
}
