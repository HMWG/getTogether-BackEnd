package com.example.getTogether.service;

import com.example.getTogether.dto.*;
import com.example.getTogether.entity.*;
import com.example.getTogether.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final MeetingRecommendRepository meetingRecommendRepository;
    private final MeetingRecommendDateRepository meetingRecommendDateRepository;
    private final MeetingRecommendFinalDateRepository meetingRecommendFinalDateRepository;
    private final MeetingRecommendPlaceRepository meetingRecommendPlaceRepository;
    private final MeetingRecommendTimeRepository meetingRecommendTimeRepository;


    @Transactional
    public List<MyMeetingDto> findMeetings(Long id){

        List<MyMeetingDto> meetings = meetingMemberRepository.findByMemberId(id).stream()
                .map(meetingMember -> MyMeetingDto.builder()
                        .id(meetingMember.getMeeting().getId())
                        .meetingType(meetingMember.getMeeting().getMeetingType())
                        .name(meetingMember.getMeeting().getName())
                        .description(meetingMember.getMeeting().getDescription())
                        .place(meetingMember.getMeeting().getPlace())
                        .start(meetingMember.getMeeting().getStart())
                        .end(meetingMember.getMeeting().getEnd())
                        .done(meetingMember.getMeeting().getDone())
                        .createdBy(meetingMember.getMeeting().getCreatedBy())
                        .count(countMember(meetingMember.getMeeting().getId()))
                        .build()).sorted(Comparator.comparing(MyMeetingDto::getId).reversed()).toList();

        if(meetings.isEmpty()){
            System.out.println("나의 모임이 존재하지 않습니다.");
        }

        return meetings;
    }

    @Transactional
    public MyMeetingDto findMeeting(Long id){

        Optional<Meeting> myMeeting = meetingRepository.findById(id);

        MyMeetingDto meeting = MyMeetingDto.builder()
                        .id(myMeeting.get().getId())
                        .meetingType(myMeeting.get().getMeetingType())
                        .name(myMeeting.get().getName())
                        .description(myMeeting.get().getDescription())
                        .place(myMeeting.get().getPlace())
                        .start(myMeeting.get().getStart())
                        .end(myMeeting.get().getEnd())
                        .done(myMeeting.get().getDone())
                        .createdBy(myMeeting.get().getCreatedBy())
                        .count(countMember(myMeeting.get().getId()))
                        .build();

        if(meeting==null){
            System.out.println("나의 모임이 존재하지 않습니다.");
        }

        return meeting;
    }

    @Transactional
    public List<MyMeetingDto> findMyMeetings(Long id){

        List<Meeting> meetings = meetingRepository.findByCreatedBy(id);

        List<MyMeetingDto> myMeetingDtos = meetings.stream()
                .map(meeting -> MyMeetingDto.builder()
                        .id(meeting.getId())
                        .meetingType(meeting.getMeetingType())
                        .name(meeting.getName())
                        .description(meeting.getDescription())
                        .place(meeting.getPlace())
                        .start(meeting.getStart())
                        .end(meeting.getEnd())
                        .done(meeting.getDone())
                        .createdBy(meeting.getCreatedBy())
                        .count(countMember(meeting.getId()))
                        .build()
                ).sorted(Comparator.comparing(MyMeetingDto::getId).reversed()).toList();

        if(meetings.isEmpty()){
            System.out.println("내가 만든 모임이 존재하지 않습니다.");
        }

        return myMeetingDtos;
    }

    @Transactional
    public void createMeeting(MeetingCreateDto meetingCreateDto){
        if (meetingCreateDto.getStart()!=null){
            if(!checkStart(meetingCreateDto)){
                throw new RuntimeException("끝나는 시간이 시작 시간보다 빠릅니다.");
            }
        }
        MeetingMember meetingMember;

        Meeting meeting = Meeting.builder()
                .name(meetingCreateDto.getName())
                .description(meetingCreateDto.getDescription())
                .place(meetingCreateDto.getPlace())
                .meetingType(meetingCreateDto.getMeetingType())
                .createdBy(meetingCreateDto.getCreatedBy())
                .done(meetingCreateDto.getDone())
                .start(meetingCreateDto.getStart())
                .end(meetingCreateDto.getEnd())
                .build();

        meetingRepository.save(meeting);

        if (memberRepository.findById(meetingCreateDto.getCreatedBy()).isPresent()){
            meetingMember = MeetingMember.builder()
                    .member(memberRepository.findById(meetingCreateDto.getCreatedBy()).get())
                    .meeting(meeting).build();
        }
        else
            throw new RuntimeException("만든 사람이 없습니다.");

        meetingMemberRepository.save(meetingMember);

        boolean isDate = false;
        boolean isTime = false;
        boolean isPlace = false;
        boolean isFinal = false;

        if(meeting.getStart() == null){
            isDate = true;
        }
        if (!meetingCreateDto.getCheckTime()){
            isTime = true;
            if(isDate){
                MeetingRecommendFinalDate meetingRecommendFinalDate = MeetingRecommendFinalDate.builder()
                        .meetingRecommend(meetingRecommendRepository.findByMeetingId(meeting.getId()).get())
                        .meetingDate(meeting.getStart())
                        .build();

                meetingRecommendFinalDateRepository.save(meetingRecommendFinalDate);
            }
        }
        if(meeting.getPlace() == null){
            isPlace = true;
            isFinal = true;
        }

        MeetingRecommend meetingRecommend = MeetingRecommend.builder()
                .meeting(meeting)
                .isDate(isDate)
                .isTime(isTime)
                .isPlace(isPlace)
                .isFinal(isFinal)
                .checkMember(0)
                .build();

        meetingRecommendRepository.save(meetingRecommend);


        checkMeetingType(meeting);

        System.out.println("모임을 생성하였습니다. 이름 : " + meeting.getName());
    }


    @Transactional
    public Boolean checkStart(MeetingCreateDto meetingCreateDto){

        LocalDateTime start = meetingCreateDto.getStart();
        LocalDateTime end = meetingCreateDto.getEnd();

        if(end.isBefore(start))
            return false;
        else
            return true;
    }

    public int countMember(Long meetingId){
        int result = 0;

        List<MeetingMember> meetingMembers = meetingMemberRepository.findByMeetingId(meetingId);

        result=meetingMembers.size();

        return result;
    }

    @Transactional
    public void deleteMeeting(NoticeIdDto noticeIdDto){

        List<MeetingRecommendDate> meetingRecommendDates = meetingRecommendDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(noticeIdDto.getId()).get().getId());
        List<MeetingRecommendFinalDate> meetingRecommendFinalDates = meetingRecommendFinalDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(noticeIdDto.getId()).get().getId());
        List<MeetingRecommendPlace> meetingRecommendPlaces = meetingRecommendPlaceRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(noticeIdDto.getId()).get().getId());
        List<MeetingRecommendTime> meetingRecommendTimes = meetingRecommendTimeRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(noticeIdDto.getId()).get().getId());

        Optional<MeetingRecommend> meetingRecommends = meetingRecommendRepository.findByMeetingId(noticeIdDto.getId());

        List<MeetingMember> meetingMembers = meetingMemberRepository.findByMeetingId(noticeIdDto.getId());
        List<Notice> notices = noticeRepository.findByMeetingId(noticeIdDto.getId());

//        for (MeetingMember meetingMember : meetingMembers) {
//            meetingMemberRepository.deleteById(meetingMember.getId());  //여기 문제가 있는 것 같음
//        }

        meetingRecommendDateRepository.deleteAll(meetingRecommendDates);
        meetingRecommendFinalDateRepository.deleteAll(meetingRecommendFinalDates);
        meetingRecommendPlaceRepository.deleteAll(meetingRecommendPlaces);
        meetingRecommendTimeRepository.deleteAll(meetingRecommendTimes);

        meetingRecommendRepository.delete(meetingRecommends.get());

        meetingMemberRepository.deleteAll(meetingMembers);
        noticeRepository.deleteAll(notices);

        meetingRepository.deleteById(noticeIdDto.getId());
    }

    @Transactional
    public void leaveMeeting(LeaveMeetingDto leaveMeetingDto){
        Optional<MeetingMember> meetingMember = meetingMemberRepository.findByMemberIdAndMeetingId(leaveMeetingDto.getMemberId(), leaveMeetingDto.getMeetingId());

        meetingMemberRepository.deleteById(meetingMember.get().getId());
    }

    @Transactional
    public void checkMeetingType(Meeting myMeeting) {
        Meeting meeting = meetingRepository.findById(myMeeting.getId()).orElseThrow(()-> new RuntimeException("해당 모임이 없습니다."));

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(myMeeting.getId()).orElseThrow(()-> new RuntimeException("해당 모임 추천이 없습니다."));

        if (meetingRecommend.getIsDate()){
            meeting.updateMeetingType(MeetingType.date);
        } else if (meetingRecommend.getIsTime()) {
            meeting.updateMeetingType(MeetingType.time);
        } else if (meetingRecommend.getIsPlace()) {
            meeting.updateMeetingType(MeetingType.place);
        } else if (meetingRecommend.getIsFinal()) {
            meeting.updateMeetingType(MeetingType.finalPlace);
        } else{
            meeting.updateMeetingType(MeetingType.toDo);
        }

        meetingRepository.save(meeting);
    }
}
