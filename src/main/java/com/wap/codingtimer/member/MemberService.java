package com.wap.codingtimer.member;

import com.wap.codingtimer.auth.domain.SocialLoginType;
import com.wap.codingtimer.member.domain.Friend;
import com.wap.codingtimer.member.domain.FriendRelation;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.dto.MemberInfo;
import com.wap.codingtimer.member.repository.FriendRepository;
import com.wap.codingtimer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public MemberInfo getMemberInfo(String memberId) {
        Member member = memberRepository.findById(memberId).get();

        return new MemberInfo(memberId, member.getNickname(), member.getSocialLoginType(), member.getPoints());
    }

    public boolean isDuplicate(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    @Transactional
    public String register(String id, String pw, String nickname) {
        String encode = bCryptPasswordEncoder.encode(pw);
        return memberRepository.save(new Member(id, encode, nickname)).getId();
    }

    @Transactional
    public String register(String snsId, SocialLoginType socialLoginType, String nickname) {
        return memberRepository.save(new Member(snsId, socialLoginType, nickname)).getId();
    }

    public String getNickname(String memberId) {
        return memberRepository.findById(memberId).get().getNickname();
    }

    public Long getPoints(String memberId) {
        return memberRepository.findById(memberId).get().getPoints();
    }

    @Transactional
    public String changeNickname(String memberId, String nickname) {
        Member member = memberRepository.findById(memberId).get();
        member.setNickname(nickname);

        return memberId;
    }

    public List<Member> getFriends(String memberId) {
        return friendRepository.findFriends(memberId);
    }

    public List<String> getRequestedNames(String memberId) {
        return friendRepository.findMemberRequested(memberId)
                .stream()
                .map(Friend::getSecondMember)
                .map(Member::getNickname)
                .toList();
    }

    public List<String> getReceivedNames(String memberId) {
        return friendRepository.findMemberReceived(memberId)
                .stream()
                .map(Friend::getFirstMember)
                .map(Member::getNickname)
                .toList();
    }

    @Transactional
    public Friend requestFriend(String memberId, String nickname) {
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
    public void deleteFriend(String memberId, String nickname) {
        String nicknameId = memberRepository.findByNickname(nickname).getId();
        Friend friend = friendRepository.findRelation(memberId, nicknameId).get();

        friendRepository.delete(friend);
    }

    @Transactional
    public void acceptRequest(String memberId, String nickname) {
        String nicknameId = memberRepository.findByNickname(nickname).getId();
        Friend friend = friendRepository.findRelation(memberId, nicknameId).get();

        friend.acceptFriend();
    }
}
