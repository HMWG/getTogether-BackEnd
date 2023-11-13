package com.example.getTogether.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetingRecommendFinalDate")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRecommendFinalDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingRecommendId", nullable = false)
    private MeetingRecommend meetingRecommend;

    @Column(nullable = false)
    private LocalDateTime meetingDate;
}
