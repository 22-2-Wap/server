package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository <Comment, Long> {

}
