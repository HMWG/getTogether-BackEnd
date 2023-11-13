package com.example.getTogether.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meetingRecommend")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingId", nullable = false)
    private Meeting meeting;

    @Column(nullable = false)
    private Boolean isDate;

    @Column(nullable = false)
    private Boolean isTime;

    @Column(nullable = false)
    private Boolean isPlace;

    @Column(nullable = false)
    private Boolean isFinal;

    @Column(nullable = true)
    private Double finalLat;

    @Column(nullable = true)
    private Double finalLng;

    @Column(nullable = false)
    private int checkMember;

    public void updateCheckMember(int checkMember){
        this.checkMember = checkMember;
    }

    public void updateIsDate(Boolean isDate) {this.isDate = isDate;}
    public void updateIsTime(Boolean isTime) {this.isTime = isTime;}
    public void updateIsPlace(Boolean isPlace) {this.isPlace = isPlace;}

    public void updateFinalLat(Double finalLat) {this.finalLat = finalLat;}

    public void updateFinalLng(Double finalLng) {this.finalLng = finalLng;}

    public void updateIsFinal(Boolean isFinal) {this.isFinal = isFinal;}


}
