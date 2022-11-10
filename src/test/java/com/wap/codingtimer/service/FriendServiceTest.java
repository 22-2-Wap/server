package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Friend;
import com.wap.codingtimer.domain.FriendRelation;
import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.repository.FriendRepository;
import com.wap.codingtimer.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class FriendServiceTest {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FriendService friendService;

    List<Member> members = new ArrayList<>();

    @BeforeEach
    void setUp() {
        IntStream.range(0, 5).forEach(o -> {
            Member member = new Member();
            member.setNickname(String.valueOf(o));
            memberRepository.save(member);
            members.add(member);
        });

        friendRepository.save(new Friend(members.get(0), members.get(1), FriendRelation.ACCEPT));
        friendRepository.save(new Friend(members.get(0), members.get(2), FriendRelation.ACCEPT));
        friendRepository.save(new Friend(members.get(3), members.get(0), FriendRelation.ACCEPT));
        friendRepository.save(new Friend(members.get(1), members.get(2), FriendRelation.ACCEPT));
        friendRepository.save(new Friend(members.get(3), members.get(1), FriendRelation.ACCEPT));
    }

    @Test
    @DisplayName("ViewFriends")
    public void 친구관계_출력() throws Exception {
        //given
        Member member = members.get(0);

        //when
        List<Member> friends = friendService.getFriends(member.getId());

        //then
        assertThat(friends).contains(members.get(1), members.get(2), members.get(3));
    }

    @Test
    void 친구_거절하기_전에_친구_요청_취소하면_예외발생() throws Exception {
        //given
        Member member = memberRepository.save(new Member());
        member.setNickname("서경룡");
        Member member2 = memberRepository.save(new Member());
        member2.setNickname("동경룡");
        friendService.request(member.getId(), member2.getNickname());

        //when
        String name = friendService.getReceivedOfferNames(member2.getId()).get(0);
        friendService.delete(member.getId(), member2.getNickname());

        //then
        assertThrows(NoSuchElementException.class, () -> friendService.delete(member2.getId(), name));
    }
}