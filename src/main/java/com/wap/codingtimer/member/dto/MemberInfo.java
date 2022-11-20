package com.wap.codingtimer.member.dto;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MemberInfo {
    private final String id;
    private final String nickname;
    private final SocialLoginType loginType;
    private final Long points;
}
