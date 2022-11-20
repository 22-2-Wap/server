package com.wap.codingtimer.auth.domain;

import org.springframework.stereotype.Component;

@Component
public class KakaoOauth implements SocialOauth{
    @Override
    public String getOauthRedirectUrl() {
        return "";
    }

    @Override
    public String requestAccessToken(String code) {
        return null;
    }

    @Override
    public String getUserEmail(String token) {
        return null;
    }
}
