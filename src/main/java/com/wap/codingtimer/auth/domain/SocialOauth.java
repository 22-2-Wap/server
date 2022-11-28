package com.wap.codingtimer.auth.domain;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialOauth {
    String getOauthRedirectUrl();

    String requestAccessToken(String code);

    String getUserEmail(String token) throws JsonProcessingException;
}
