package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findLikesByMemberIdAndPostId(Long memberId, Long postId);

    int countLikesByPostId(Long postId);
}
