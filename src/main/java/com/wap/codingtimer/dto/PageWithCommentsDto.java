package com.wap.codingtimer.dto;

import com.wap.codingtimer.domain.Comment;
import com.wap.codingtimer.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PageWithCommentsDto {
    private final Post post;
    private final List<Comment> comments;
}
