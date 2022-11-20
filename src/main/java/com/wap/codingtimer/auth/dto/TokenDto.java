package com.wap.codingtimer.auth.dto;

import com.wap.codingtimer.auth.domain.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private String nickname;

    public TokenDto(String accessToken, String refreshToken, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
    }

    public long getRefreshTokenExpirationTime() {
        return Jwts.parser()
                .setSigningKey(JwtTokenProvider.getSecret())
                .parseClaimsJws(this.refreshToken)
                .getBody()
                .getExpiration()
                .getTime();
    }
}
