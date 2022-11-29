package com.wap.codingtimer.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PageWithCommentsDto {
    private final PostDto post;
    private final List<CommentDto> comments;
    private boolean isLikePressed;

    public void setLikePressed(boolean likePressed) {
        isLikePressed = likePressed;
    }
}
