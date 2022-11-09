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
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public String[] getFriendsNames(Long memberId) {

        List<Friend> requestFriends = friendRepository.findFriendsByFirstMemberIdAndRelationIs(
                memberId, FriendRelation.ACCEPT);

        List<Friend> responseFriends = friendRepository.findFriendsBySecondMemberIdAndRelationIs(
                memberId, FriendRelation.ACCEPT);

        return Stream.concat(
                requestFriends.stream()
                        .map(Friend::getSecondMember),
                responseFriends.stream()
                        .map(Friend::getFirstMember))
                .map(Member::getNickname)
                .toArray(String[]::new);
    }


    public String[] getRequestNames(Long memberId){
        List<Friend> requestFriends = friendRepository.findFriendsByFirstMemberIdAndRelationIs(
                memberId, FriendRelation.REQUEST);

        return requestFriends.stream()
                .map(Friend::getSecondMember)
                .map(Member::getNickname)
                .toArray(String[]::new);
    }

    @Transactional
    public Friend request(Long memberId, String nickname) throws RuntimeException{
        if(!memberRepository.existsMemberByNickname(nickname))
            throw new NoSuchElementException("해당 유저가 존재하지 않습니다.");

        long nicknameId = memberRepository.findByNickname(nickname).getId();

        if(friendRepository.existsByFirstMember_IdAndSecondMember_Id(memberId, nicknameId))
            throw new NoSuchElementException("이미 요청되거나 추가된 친구입니다.");

        Member member1 = memberRepository.findById(memberId).get();
        Member member2 = memberRepository.findById(nicknameId).get();
        Friend friend1 = new Friend(member1, member2, FriendRelation.REQUEST);
        return friendRepository.save(friend1);
    }

    @Transactional
    public void revert(Long memberId, String nickname) throws RuntimeException{
        long nicknameId = memberRepository.findByNickname(nickname).getId();


        Optional<Friend> friend1 = friendRepository.findByFirstMember_IdAndSecondMember_Id(memberId, nicknameId);
        Optional<Friend> friend2 = friendRepository.findByFirstMember_IdAndSecondMember_Id(nicknameId, memberId);
        if(friend1.isPresent())
            friendRepository.delete(friend1.get());
        if(friend2.isPresent())
            friendRepository.delete(friend2.get());
    }

    @Transactional
    public void acceptRequest(Long memberId, String nickname) throws RuntimeException{
        long nicknameId = memberRepository.findByNickname(nickname).getId();

        Optional<Friend> friend = friendRepository.findByFirstMember_IdAndSecondMember_Id(memberId, nicknameId);

        friend.get().setRelation();
    }
    @Transactional
    public void rejectRequest(Long memberId, String nickname) throws RuntimeException{
        long nicknameId = memberRepository.findByNickname(nickname).getId();

        Optional<Friend> friend = friendRepository.findByFirstMember_IdAndSecondMember_Id(memberId, nicknameId);

        friendRepository.delete(friend.get());
    }

}
