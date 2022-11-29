package com.wap.codingtimer.post.repository;

import com.wap.codingtimer.post.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findLikesByMemberIdAndPostId(String memberId, Long postId);

    boolean existsByMemberIdAndPostId(String memberId, Long postId);
}
