package com.wap.codingtimer.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "first_member")
    private Member firstMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "member_id", name = "second_member")
    private Member secondMember;

    @Enumerated(EnumType.STRING)
    private FriendRelation relation;

    public Friend(Member member1, Member member2, FriendRelation friendRelation) {
        this.firstMember = member1;
        this.secondMember = member2;
        this.relation = friendRelation;
    }

    public void setRelation() {
        this.relation = FriendRelation.ACCEPT;
    }
}
