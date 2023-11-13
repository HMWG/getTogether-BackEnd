package com.example.getTogether.repository;

import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.MeetingMember;
import com.example.getTogether.entity.MeetingRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRecommendRepository extends JpaRepository<MeetingRecommend, Long> {

    Optional<MeetingRecommend> findByMeetingId(Long meetingId);
}
