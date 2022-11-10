package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Friend;
import com.wap.codingtimer.domain.FriendRelation;
import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.repository.FriendRepository;
import com.wap.codingtimer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public List<Member> getFriends(Long memberId) {
        return friendRepository.findFriends(memberId);
    }

    public List<String> getRequestedOfferNames(Long memberId) {
        return friendRepository.findMemberRequested(memberId)
                .stream()
                .map(Friend::getSecondMember)
                .map(Member::getNickname)
                .toList();
    }

    public List<String> getReceivedOfferNames(Long memberId) {
        return friendRepository.findMemberReceived(memberId)
                .stream()
                .map(Friend::getSecondMember)
                .map(Member::getNickname)
                .toList();
    }

    @Transactional
    public Friend request(Long memberId, String nickname) throws RuntimeException {
        if (!memberRepository.existsMemberByNickname(nickname))
            throw new NoSuchElementException("해당 유저가 존재하지 않습니다.");

        Member member = memberRepository.findById(memberId).get();
        Member otherMember = memberRepository.findByNickname(nickname);

        Optional<Friend> relation = friendRepository.findRelation(memberId, otherMember.getId());

        if (relation.isPresent())
            throw new NoSuchElementException("이미 요청되거나 추가된 친구입니다.");

        Friend friend = new Friend(member, otherMember, FriendRelation.REQUEST);
        return friendRepository.save(friend);
    }

    //거절 또는 친구 삭제
    @Transactional
    public void delete(Long memberId, String nickname) throws RuntimeException {
        long nicknameId = memberRepository.findByNickname(nickname).getId();
        Friend friend = friendRepository.findRelation(memberId, nicknameId).get();

        friendRepository.delete(friend);
    }

    @Transactional
    public void acceptRequest(Long memberId, String nickname) throws RuntimeException {
        long nicknameId = memberRepository.findByNickname(nickname).getId();
        Friend friend = friendRepository.findRelation(memberId, nicknameId).get();

        friend.setRelation();
    }
}
