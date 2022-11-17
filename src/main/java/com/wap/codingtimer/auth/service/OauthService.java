//package com.wap.codingtimer.auth.service;
//
//import com.wap.codingtimer.auth.domain.GoogleOauth;
//import com.wap.codingtimer.auth.domain.KakaoOauth;
//import com.wap.codingtimer.auth.domain.SocialLoginType;
//import com.wap.codingtimer.auth.domain.SocialOauth;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class OauthService {
//    private final GoogleOauth googleOauth;
//    private final KakaoOauth kakaoOauth;
//
//    private final HttpServletResponse response;
//
//    public void request(SocialLoginType socialLoginType) throws IOException {
//        String oauthRedirectUrl = getSocialOauth(socialLoginType).getOauthRedirectUrl();
//
//        response.sendRedirect(oauthRedirectUrl);
//    }
//
//    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
//        return getSocialOauth(socialLoginType).requestAccessToken(code);
//    }
//
//    private SocialOauth getSocialOauth(SocialLoginType socialLoginType) {
//        if(socialLoginType==SocialLoginType.GOOGLE)
//            return googleOauth;
//
//        if(socialLoginType==SocialLoginType.KAKAO)
//            return kakaoOauth;
//
//        return null;
//    }
//}
