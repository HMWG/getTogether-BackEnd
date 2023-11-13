package com.example.getTogether.repository;

import com.example.getTogether.entity.MeetingRecommendDate;
import com.example.getTogether.entity.MeetingRecommendFinalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRecommendFinalDateRepository extends JpaRepository<MeetingRecommendFinalDate, Long> {
    List<MeetingRecommendFinalDate> findByMeetingRecommendId(Long meetingRecommendId);
}
