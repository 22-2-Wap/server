package com.wap.codingtimer.post.dto;

import com.wap.codingtimer.post.domain.Comment;
import com.wap.codingtimer.post.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@ToString
public class PageWithCommentsDto {
    private final Post post;
    private final List<Comment> comments;
}
