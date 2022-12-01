package com.wap.codingtimer.post.dto;

import com.wap.codingtimer.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String member;
    private int likes;
    private String topic;
    private String content;
    private String category;
    private LocalDateTime dateTime;

    public PostDto(Post post) {
        this.id = post.getId();
        this.member = post.getMember().getNickname();
        this.likes = post.getLikes().size();
        this.topic = post.getTopic();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.dateTime = post.getDateTime();
    }

    public PostDto(Long id, String member, int likes, String topic, String category, LocalDateTime dateTime) {
        this.id = id;
        this.member = member;
        this.likes = likes;
        this.topic = topic;
        this.category = category;
        this.dateTime = dateTime;
    }
}
