package com.example.getTogether.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJoinDto {

    private Long id;

    private String username;
}
