package com.wap.codingtimer.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wap.codingtimer.auth.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final JwtTokenProvider jwtTokenProvider;

    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) throws IOException {
        String oauthRedirectUrl = getSocialOauth(socialLoginType).getOauthRedirectUrl();

        response.sendRedirect(oauthRedirectUrl);
    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = getSocialOauth(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }

    public String requestUserInfo(SocialLoginType socialLoginType, String token) throws JsonProcessingException {
        SocialOauth socialOauth = getSocialOauth(socialLoginType);
        return socialOauth.getUserEmail(token);
    }
    private SocialOauth getSocialOauth(SocialLoginType socialLoginType) {
        if(socialLoginType==SocialLoginType.GOOGLE)
            return googleOauth;

        if(socialLoginType==SocialLoginType.KAKAO)
            return kakaoOauth;

        return null;
    }

    public String getUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.parseClaims(token).getSubject();
    }
}
