package com.wap.codingtimer.domain;

import lombok.Getter;

import javax.persistence.*;

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
}
