package com.example.getTogether.repository;

import com.example.getTogether.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    List<MeetingMember> findByMemberId(Long memberId);
    List<MeetingMember> findByMeetingId(Long meetingId);

    Optional<MeetingMember> findByMemberIdAndMeetingId(Long memberId, Long meetingId);
}
