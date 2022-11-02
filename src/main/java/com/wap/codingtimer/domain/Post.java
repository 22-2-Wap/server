package com.wap.codingtimer.domain;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String topic;
    private String content;
    private String category;
    private int likes;
    private LocalDateTime dateTime;

    /**
     * 비즈니스 로직
     */
    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }

}
