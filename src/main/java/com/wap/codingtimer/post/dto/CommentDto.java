package com.wap.codingtimer.post.dto;

import com.wap.codingtimer.post.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String member;
    private String content;
    private LocalDateTime dateTime;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.member = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.dateTime = comment.getDateTime();
    }
}
