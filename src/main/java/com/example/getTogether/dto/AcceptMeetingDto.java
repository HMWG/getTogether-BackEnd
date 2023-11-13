package com.example.getTogether.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcceptMeetingDto {

    private Long noticeId;

    private Long memberId;
}
