package com.wap.codingtimer.post.repository;

import com.wap.codingtimer.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository <Post,Long> {
    Page<Post> findAllByCategory(String category, Pageable pageable);

    Page<Post> findAllByMemberId(String memberId, Pageable pageable);
}
