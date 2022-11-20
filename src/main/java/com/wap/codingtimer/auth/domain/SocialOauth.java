package com.wap.codingtimer.auth.domain;

public interface SocialOauth {
    String getOauthRedirectUrl();

    String requestAccessToken(String code);

    String getUserEmail(String token);
}
