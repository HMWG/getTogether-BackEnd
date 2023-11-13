package com.example.getTogether.repository;

import com.example.getTogether.entity.MeetingRecommend;
import com.example.getTogether.entity.MeetingRecommendDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRecommendDateRepository extends JpaRepository<MeetingRecommendDate, Long> {
    List<MeetingRecommendDate> findByMeetingRecommendId(Long meetingRecommendId);

    List<MeetingRecommendDate> findByMeetingRecommendIdAndMemberId(Long meetingRecommendId, Long memberId);
}
