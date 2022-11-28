package com.wap.codingtimer.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {
    @Value("${sns.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${sns.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${sns.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${sns.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${sns.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;
    @Value("${sns.google.info.url}")
    private String GOOGLE_SNS_USER_INFO_URL;

    @Override
    public String getOauthRedirectUrl() {
        StringBuilder url = new StringBuilder(GOOGLE_SNS_BASE_URL);

        url.append("?")
                .append("scope=email&")
                .append("response_type=code&")
                .append("client_id=" + GOOGLE_SNS_CLIENT_ID + "&")
                .append("redirect_uri=" + GOOGLE_SNS_CALLBACK_URL);

        return url.toString();
    }

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

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

        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_SNS_USER_INFO_URL,
                HttpMethod.GET,
                request,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK)
            return (String) objectMapper.readValue(response.getBody(), Map.class).get("email");

        throw new NoSuchElementException("구글 로그인 이메일 가져오기 실패");
    }
}
