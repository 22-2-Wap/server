package com.wap.codingtimer.post.domain;

import com.wap.codingtimer.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Likes {
    @Id
    @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 도메인 로직
     */
    public void setMember(Member member) {
        this.member = member;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
