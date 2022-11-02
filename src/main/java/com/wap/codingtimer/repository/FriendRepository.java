package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Friend;
import com.wap.codingtimer.domain.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findFriendsByFirstMemberIdAndRelationIs(Long member_id, FriendRelation relation);

    List<Friend> findFriendsBySecondMemberIdAndRelationIs(Long member_id, FriendRelation relation);
}
