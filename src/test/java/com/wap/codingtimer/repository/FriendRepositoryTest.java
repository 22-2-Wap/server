package com.wap.codingtimer.repository;

import com.wap.codingtimer.member.domain.Friend;
import com.wap.codingtimer.member.domain.FriendRelation;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.FriendRepository;
import com.wap.codingtimer.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FriendRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FriendRepository friendRepository;

    @Test
    void 친구_관계_조회() throws Exception {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        friendRepository.save(new Friend(member1, member2, FriendRelation.ACCEPT));

        //when
        List<Member> friends = friendRepository.findFriends(member1.getId());

        //then
        assertThat(friends.size()).isOne();
        assertThat(friends.get(0)).isEqualTo(member2);
    }

    @Test
    void 친구_신청_조회() throws Exception {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        friendRepository.save(new Friend(member1, member2, FriendRelation.REQUEST));

        //when
        Friend member2Received = friendRepository.findMemberReceived(member2.getId()).get(0);
        Friend member1Requested = friendRepository.findMemberRequested(member1.getId()).get(0);

        //then
        assertThat(member1Requested).isEqualTo(member2Received);
    }

    @Test
    void 친구_신청_여부_조회() throws Exception {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        friendRepository.save(new Friend(member1, member2, FriendRelation.REQUEST));

        //when
        Optional<Friend> relation = friendRepository.findRelation(member1.getId(), member2.getId());

        //then
        assertThat(relation).isNotEmpty();
    }

    Member createMember() {
        return memberRepository.save(new Member("id", "pw", "nickname"));
    }
}