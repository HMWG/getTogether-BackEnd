package com.example.getTogether.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceDto {

    private Double latitude;

    private Double longitude;

    private Long createdBy;
}
