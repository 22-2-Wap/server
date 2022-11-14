package com.wap.codingtimer.member.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String snsId;

    @Column(unique = true)
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
