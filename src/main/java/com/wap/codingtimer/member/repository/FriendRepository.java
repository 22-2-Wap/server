package com.wap.codingtimer.member.repository;

import com.wap.codingtimer.member.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>, FriendCustomRepository {
}
