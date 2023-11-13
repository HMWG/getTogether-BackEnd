package com.example.getTogether.repository;

import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    void deleteAllByMemberId(Long memberId);

    List<Notice> findByMemberId(Long id);

    List<Notice> findByMeetingId(Long id);
}
