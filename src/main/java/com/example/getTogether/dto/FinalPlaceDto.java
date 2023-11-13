package com.example.getTogether.dto;

import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinalPlaceDto{
    private Long meetingId;

    private String place;
}
