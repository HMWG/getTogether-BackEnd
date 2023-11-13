package com.example.getTogether.service;

import com.example.getTogether.dto.InviteMeetingDto;
import com.example.getTogether.dto.MyMeetingDto;
import com.example.getTogether.dto.MyNoticeDto;
import com.example.getTogether.dto.NoticeIdDto;
import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.MeetingMember;
import com.example.getTogether.entity.Member;
import com.example.getTogether.entity.Notice;
import com.example.getTogether.repository.MeetingMemberRepository;
import com.example.getTogether.repository.MeetingRepository;
import com.example.getTogether.repository.MemberRepository;
import com.example.getTogether.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;


    @Transactional
    public void invite(InviteMeetingDto inviteMeetingDto){
        Notice notice = Notice.builder()
                .member(memberRepository.findById(inviteMeetingDto.getMemberId()).get())
                .invitor(memberRepository.findById(inviteMeetingDto.getInvitorId()).get())
                .meeting(meetingRepository.findById(inviteMeetingDto.getMeetingId()).get())
                .isView(true)
                .build();

        noticeRepository.save(notice);
        System.out.printf("초대 완료 invitorID : %d memberId : %d meetingId : %s%n", notice.getInvitor().getId(), notice.getMember().getId(), notice.getMeeting().getName());
    }

    @Transactional
    public List<MyNoticeDto> getNotice(Long memberId){
        List<Notice> notices = noticeRepository.findByMemberId(memberId);

        List<MyNoticeDto> myNotice = notices.stream().map(
                notice -> MyNoticeDto.builder()
                        .id(notice.getId())
                        .invitor(notice.getInvitor().getUsername())
                        .meetingName(notice.getMeeting().getName())
                        .isView(notice.getIsView())
                        .build()
        ).sorted(Comparator.comparing(MyNoticeDto::getId).reversed()).toList();

        if(notices.isEmpty()){
            System.out.println("알림이 존재하지 않습니다.");
        }

        return myNotice;
    }

    @Transactional
    public void clearNotice(Member member){
        noticeRepository.deleteAllByMemberId(member.getId());
    }

    @Transactional
    public void deleteNotice(NoticeIdDto noticeIdDto){
        noticeRepository.deleteById(noticeIdDto.getId());
    }

    @Transactional
    public void viewNotice(NoticeIdDto noticeIdDto){
        Notice notice = noticeRepository.findById(noticeIdDto.getId()).orElseThrow(()-> new IllegalArgumentException("해당 알림이 없습니다."));

        Notice viewNotice = Notice.builder()
                .isView(false)
                .id(notice.getId())
                .meeting(notice.getMeeting())
                .invitor(notice.getInvitor())
                .member(notice.getMember()).build();

        noticeRepository.save(viewNotice);
        System.out.println("모임 확인");
    };

    @Transactional
    public void accept(Long noticeId, Long memberId){

        Optional<MeetingMember> checkMeetingMember;

        checkMeetingMember = meetingMemberRepository.findByMemberIdAndMeetingId(memberId, noticeRepository.findById(noticeId).get().getMeeting().getId());

        if (checkMeetingMember.isEmpty()){
            MeetingMember meetingMember = MeetingMember.builder()
                    .meeting(noticeRepository.findById(noticeId).get().getMeeting())
                    .member(memberRepository.findById(memberId).get())
                    .build();

            meetingMemberRepository.save(meetingMember);
            System.out.printf("모임 가입 수락 완료 meetingName : %s member : %d%n%n", meetingMember.getMeeting().getName(), meetingMember.getMember().getId());

        }
        else
            throw new RuntimeException("이미 수락한 모임입니다.");


        }
}
