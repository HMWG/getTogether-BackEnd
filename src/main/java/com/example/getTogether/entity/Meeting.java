package com.example.getTogether.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meeting")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String place;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private MeetingType meetingType;

    @Column(nullable = true)
    private LocalDateTime start;

    @Column(nullable = true)
    private LocalDateTime end;

    @Column(nullable = false)
    private Boolean done;

    @Column(nullable = false)
    private Long createdBy;

    public void updateMeetingType(MeetingType meetingType){
        this.meetingType = meetingType;
    }

    public void updateStart(LocalDateTime start){
        this.start = start;
    }

    public void updateEnd(LocalDateTime end){
        this.end = end;
    }

    public void updatePlace(String place){
        this.place = place;
    }


}
