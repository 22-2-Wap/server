package com.wap.codingtimer.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wap.codingtimer.member.domain.Friend;
import com.wap.codingtimer.member.domain.FriendRelation;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.domain.QFriend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendCustomRepositoryImpl implements FriendCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QFriend friend = QFriend.friend;

    @Override
    public List<Member> findFriends(String memberId) {
        return jpaQueryFactory.selectFrom(friend)
                .where(friend.relation.eq(FriendRelation.ACCEPT)
                        .and(friend.firstMember.id.eq(memberId))
                        .or(friend.secondMember.id.eq(memberId)))
                .fetch()
                .stream()
                .map(o -> {
                    if (o.getFirstMember().getId().equals(memberId))
                        return o.getSecondMember();
                    return o.getFirstMember();
                })
                .toList();
    }

    @Override
    public List<Friend> findMemberRequested(String memberId) {
        return jpaQueryFactory.selectFrom(friend)
                .where(friend.relation.eq(FriendRelation.REQUEST))
                .where(friend.firstMember.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Friend> findMemberReceived(String memberId) {
        return jpaQueryFactory.selectFrom(friend)
                .where(friend.relation.eq(FriendRelation.REQUEST))
                .where(friend.secondMember.id.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<Friend> findRelation(String memberId, String otherMemberId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(friend)
                .where(friend.firstMember.id.eq(memberId)
                        .or(friend.secondMember.id.eq(memberId)))
                .where(friend.firstMember.id.eq(otherMemberId)
                        .or(friend.secondMember.id.eq(otherMemberId)))
                .fetchOne());
    }
}
