package com.wap.codingtimer.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class KakaoOauth implements SocialOauth{
    @Value("${sns.kakao.url}")
    private String KAKAO_SNS_BASE_URL;
    @Value("${sns.kakao.client.id}")
    private String KAKAO_SNS_CLIENT_ID;
    @Value("${sns.kakao.client.secret}")
    private String KAKAO_SNS_CLIENT_SECRET;
    @Value("${sns.kakao.callback.url}")
    private String KAKAO_SNS_CALLBACK_URL;
    @Value("${sns.kakao.token.url}")
    private String KAKAO_SNS_TOKEN_BASE_URL;
    @Value("${sns.kakao.info.url}")
    private String KAKAO_SNS_USER_INFO_URL;

    @Override
    public String getOauthRedirectUrl() {
        StringBuilder url = new StringBuilder(KAKAO_SNS_BASE_URL);

        url.append("?")
                .append("response_type=code&")
                .append("scope=account_email&")
                .append("client_id=" + KAKAO_SNS_CLIENT_ID + "&")
                .append("redirect_uri=" + KAKAO_SNS_CALLBACK_URL + "&")
                .append("prompt=login");

        return url.toString();
    }

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("client_secret", KAKAO_SNS_CLIENT_SECRET);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(KAKAO_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK)
            return responseEntity.getBody();

        return "";
    }

    @Override
    public String getUserEmail(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map values = objectMapper.readValue(token, Map.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+values.get("access_token"));
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(KAKAO_SNS_USER_INFO_URL,
                HttpMethod.GET,
                request,
                String.class);

        System.out.println(response.getBody());

        if (response.getStatusCode() == HttpStatus.OK)
            return (String) objectMapper.readValue(response.getBody(), Map.class).get("email");

        throw new NoSuchElementException("카카오 로그인 이메일 가져오기 실패");
    }
}
