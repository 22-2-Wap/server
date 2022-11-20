package com.wap.codingtimer.timer;

import com.wap.codingtimer.auth.service.OauthService;
import com.wap.codingtimer.timer.dto.CurrentTimerStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("timer")
@RequiredArgsConstructor
public class TimerController {

    private final OauthService oauthService;
    private final TimerService timerService;

    @GetMapping
    public CurrentTimerStatusDto getInfo(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        return timerService.getCurrentUserStatus(userId);
    }

    @GetMapping("start")
    public String start(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        timerService.start(userId);

        return "OK";
    }

    @GetMapping("stop")
    public String stop(HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        timerService.start(userId);

        return "OK";
    }
}
