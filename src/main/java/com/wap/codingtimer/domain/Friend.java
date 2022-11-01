package com.wap.codingtimer.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Friend {

    @Id
    @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "first_member")
    private Member firstMember;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "second_member")
    private Member secondMember;

    @Enumerated(EnumType.STRING)
    private FriendRelation relation;
}
