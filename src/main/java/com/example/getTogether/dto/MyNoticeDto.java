package com.example.getTogether.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyNoticeDto {
    private Long id;

    private String invitor;

    private String meetingName;

    private boolean isView;
}
