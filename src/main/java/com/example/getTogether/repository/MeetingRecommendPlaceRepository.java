package com.example.getTogether.repository;

import com.example.getTogether.entity.MeetingRecommendDate;
import com.example.getTogether.entity.MeetingRecommendPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRecommendPlaceRepository extends JpaRepository<MeetingRecommendPlace, Long> {
    List<MeetingRecommendPlace> findByMeetingRecommendId(Long meetingRecommendId);

    List<MeetingRecommendPlace> findByMeetingRecommendIdAndMemberId(Long meetingRecommendId, Long memberId);
}
