package com.wap.codingtimer.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private int EXPIRY;
    private final UserDetailsService userDetailsService;

    public String createToken(String id, String nickname) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("nickname", nickname);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberId(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String jwtToken) {
        try {
            System.out.println("token is : "+jwtToken);
            Date expiration = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getExpiration();

            return !expiration.before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getMemberId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
