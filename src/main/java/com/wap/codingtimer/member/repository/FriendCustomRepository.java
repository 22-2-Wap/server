package com.wap.codingtimer.member.repository;

import com.wap.codingtimer.member.domain.Friend;
import com.wap.codingtimer.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FriendCustomRepository {
    List<Member> findFriends(String memberId);

    List<Friend> findMemberRequested(String memberId);

    List<Friend> findMemberReceived(String memberId);

    Optional<Friend> findRelation(String memberId, String otherMemberId);
}
