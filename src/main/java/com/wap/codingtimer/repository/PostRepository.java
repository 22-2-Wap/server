package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.Date;

public interface PostRepository extends JpaRepository <Post,Long> {

}
