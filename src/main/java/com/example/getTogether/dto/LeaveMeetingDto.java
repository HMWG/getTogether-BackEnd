package com.example.getTogether.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaveMeetingDto {

    private Long meetingId;

    private Long memberId;
}
