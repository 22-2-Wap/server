package com.wap.codingtimer.post.domain;

import com.wap.codingtimer.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Likes> likes=new ArrayList<>();

    private String topic;
    private String content;
    private String category;
    private LocalDateTime dateTime;

    /**
     * 비즈니스 로직
     */


    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
