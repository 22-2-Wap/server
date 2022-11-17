package com.wap.codingtimer.member.domain;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

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

    public Member(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.socialLoginType = SocialLoginType.LOCAL;
    }

    /**
     * 도메인 로직
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * UserDetails 구현
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
