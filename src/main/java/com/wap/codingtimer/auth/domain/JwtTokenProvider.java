package com.wap.codingtimer.auth.domain;

import com.wap.codingtimer.auth.dto.TokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private static final String SECRET = "amFlaHl1bl9qd3RfdG9rZW4K";
    private static final String AUTHORITY_KEY = "auth";
    private static final Long ACCESS_TOKEN_EXPIRY=2*60*1000L; //30분
    private static final Long REFRESH_TOKEN_EXPIRY=7*24*60*60*1000L; //7일

    public static String getSecret() {
        return SECRET;
    }

    public TokenDto generateToken(Authentication authentication, String nickname) {
        String authority = authentication.getAuthorities().toString();
        long now = new Date().getTime();

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITY_KEY, authority)
                .claim("nickname", nickname)
                .setExpiration(new Date(now+ACCESS_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return new TokenDto(accessToken, refreshToken, nickname);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(claims.get(AUTHORITY_KEY).toString());
        UserDetails principal = new User(claims.getSubject(), "", List.of(authority));

        return new UsernamePasswordAuthenticationToken(principal, "", List.of(authority));
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwtToken);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
