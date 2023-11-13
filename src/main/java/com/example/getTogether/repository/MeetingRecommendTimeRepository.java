package com.example.getTogether.repository;

import com.example.getTogether.entity.MeetingRecommendDate;
import com.example.getTogether.entity.MeetingRecommendTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRecommendTimeRepository extends JpaRepository<MeetingRecommendTime, Long> {
    List<MeetingRecommendTime> findByMeetingRecommendId(Long meetingRecommendId);

    List<MeetingRecommendTime> findByMeetingRecommendIdAndMemberId(Long meetingRecommendId, Long memberId);
}
