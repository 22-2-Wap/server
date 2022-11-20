package com.wap.codingtimer.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class RequestListDto {
    private final List<String> requested;
    private final List<String> received;
}
