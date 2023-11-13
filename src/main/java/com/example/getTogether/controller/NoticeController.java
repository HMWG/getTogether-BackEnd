package com.example.getTogether.controller;

import com.example.getTogether.dto.*;
import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.MeetingRecommend;
import com.example.getTogether.entity.Notice;
import com.example.getTogether.repository.MeetingRepository;
import com.example.getTogether.repository.MemberRepository;
import com.example.getTogether.repository.NoticeRepository;
import com.example.getTogether.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;


    @GetMapping("/notice")
    public ResponseEntity<List<MyNoticeDto>> findMeetings(@RequestParam Long id){

        List<MyNoticeDto> notices = noticeService.getNotice(id);

        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @PostMapping("/notice/invite")
    public ResponseEntity<Meeting> inviteMeeting(@RequestBody InviteMeetingDto inviteMeetingDto){

        noticeService.invite(inviteMeetingDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/notice/clear")
    public ResponseEntity<Meeting> clearMeeting(@RequestParam Long id){

        noticeService.clearNotice(memberRepository.findById(id).get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/notice/delete")
    public ResponseEntity<Meeting> deleteMeeting(@RequestBody NoticeIdDto noticeIdDto){

        noticeService.deleteNotice(noticeIdDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/notice/view")
    public ResponseEntity<Meeting> viewMeeting(@RequestBody NoticeIdDto noticeIdDto){

        noticeService.viewNotice(noticeIdDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/notice/accept")
    public ResponseEntity<NoticeIdDto> acceptMeeting(@RequestBody AcceptMeetingDto acceptMeetingDto){

        noticeService.accept(acceptMeetingDto.getNoticeId(), acceptMeetingDto.getMemberId());

        NoticeIdDto noticeIdDto = NoticeIdDto.builder().id(noticeRepository.findById(acceptMeetingDto.getNoticeId()).get().getMeeting().getId()).build();

        return new ResponseEntity<>(noticeIdDto, HttpStatus.OK);
    }






}
