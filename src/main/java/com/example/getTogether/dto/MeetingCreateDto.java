package com.example.getTogether.dto;

import com.example.getTogether.entity.MeetingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingCreateDto {

    private String name;

    private String place;

    private String description;

    private MeetingType meetingType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime end;

    private Boolean done;

    private Long createdBy;

    private Boolean checkTime;
}
