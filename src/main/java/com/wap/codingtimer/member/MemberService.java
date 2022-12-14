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

    public boolean isNicknameDuplicated(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    public boolean isIdDuplicated(String id) {
        return memberRepository.existsById(id);
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
            throw new NoSuchElementException("?????? ????????? ???????????? ????????????.");

        Member member = memberRepository.findById(memberId).get();
        Member otherMember = memberRepository.findByNickname(nickname);

        if(member.equals(otherMember))
            throw new IllegalStateException("?????? ????????? ????????? ??? ????????????.");

        Optional<Friend> relation = friendRepository.findRelation(memberId, otherMember.getId());

        if (relation.isPresent())
            throw new IllegalStateException("?????? ??????????????? ????????? ???????????????.");

        Friend friend = new Friend(member, otherMember, FriendRelation.REQUEST);
        return friendRepository.save(friend);
    }

    //?????? ?????? ?????? ??????
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

    public boolean isSnsMemberExist(String memberId, SocialLoginType socialLoginType) {
        Member member = memberRepository.findById(memberId).get();

        return member.getSocialLoginType().equals(socialLoginType);
    }
}
