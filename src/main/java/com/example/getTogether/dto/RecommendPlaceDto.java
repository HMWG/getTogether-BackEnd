package com.example.getTogether.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendPlaceDto {
    private Long memberId;

    private Long meetingId;

    private Double latitude;

    private Double longitude;
}
