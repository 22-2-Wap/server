package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Friend;
import com.wap.codingtimer.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FriendCustomRepository {
    List<Member> findFriends(Long memberId);

    List<Friend> findMemberRequested(Long memberId);

    List<Friend> findMemberReceived(Long memberId);

    Optional<Friend> findRelation(Long memberId, Long otherMemberId);
}
