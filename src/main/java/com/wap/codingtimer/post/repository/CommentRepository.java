package com.wap.codingtimer.post.repository;

import com.wap.codingtimer.post.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
    List<Comment> findAllByPostId(Long postId);
}
