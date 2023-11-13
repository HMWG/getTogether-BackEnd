package com.example.getTogether.dto;

import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteMeetingDto {

    private Long memberId;

    private Long invitorId;

    private Long meetingId;
}
