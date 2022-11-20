package com.wap.codingtimer.member.domain;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    private String id;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(unique = true)
    private String nickname;

    private Long points=0L;

    public Member(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.socialLoginType = SocialLoginType.LOCAL;
    }

    public Member(String id, SocialLoginType socialLoginType, String nickname) {
        this.id = id;
        this.socialLoginType = socialLoginType;
        this.nickname = nickname;
    }

    /**
     * 도메인 로직
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addPoints(long points) {
        this.points += points;
    }

    /**
     * UserDetails 구현
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
