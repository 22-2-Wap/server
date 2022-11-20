package com.wap.codingtimer.post.domain;

import com.wap.codingtimer.member.domain.Member;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", member=" + member.getNickname() +
                ", post=" + post.getId() +
                ", content='" + content + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    /**
     * 비즈니스 로직
     */
    public void setPost(Post post) {
        this.post = post;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }
}
