package com.wap.codingtimer.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;
    private LocalDateTime dateTime;


    /**
     * 비즈니스 로직
     */
    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    /**
     * 연관관계 메소드
     */
    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}
