package com.wap.codingtimer.member;

import com.wap.codingtimer.auth.service.OauthService;
import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.dto.FriendsInfo;
import com.wap.codingtimer.member.dto.MemberInfo;
import com.wap.codingtimer.member.dto.RequestListDto;
import com.wap.codingtimer.timer.TimerService;
import com.wap.codingtimer.timer.dto.CurrentTimerStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class MemberController {

    private final OauthService oauthService;
    private final MemberService memberService;
    private final TimerService timerService;

    @GetMapping
    public MemberInfo getInfo(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        return memberService.getMemberInfo(userId);
    }

    @GetMapping("friend")
    public List<FriendsInfo> getFriends(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        List<Member> friends = memberService.getFriends(userId);
        List<CurrentTimerStatusDto> friendsStatus = friends.stream()
                .map(Member::getId)
                .map(timerService::getCurrentUserStatus)
                .sorted(Comparator.comparingInt(CurrentTimerStatusDto::getMinutes))
                .toList();

        List<FriendsInfo> result = new ArrayList<>();
        int index=1;
        for(CurrentTimerStatusDto friend: friendsStatus)
            result.add(new FriendsInfo(index++, friend.getNickname(), friend.getMinutes(), friend.getStatus()));

        return result;
    }

    @PostMapping("{nickname}")
    public String changeNickname(@PathVariable("nickname") String nickname,
                                 HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        memberService.changeNickname(userId, nickname);

        return nickname;
    }

    @GetMapping("requests")
    public RequestListDto getRequests(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        return new RequestListDto(
                memberService.getRequestedNames(userId),
                memberService.getReceivedNames(userId));
    }

    @PostMapping("friend/{nickname}")
    public String requestFriend(@PathVariable("nickname") String nickname,
                                HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        memberService.requestFriend(userId, nickname);

        return nickname;
    }

    @DeleteMapping("friend/{nickname}")
    public String rejectFriend(@PathVariable("nickname") String nickname,
                               HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        memberService.deleteFriend(userId, nickname);

        return "OK";
    }

    @PostMapping("friend/accept/{nickname}")
    public String acceptFriend(@PathVariable("nickname") String nickname,
                               HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        memberService.acceptRequest(userId, nickname);

        return "OK";
    }
}
