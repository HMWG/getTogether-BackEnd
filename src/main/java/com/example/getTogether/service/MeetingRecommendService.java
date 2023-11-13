package com.example.getTogether.service;

import com.example.getTogether.dto.*;
import com.example.getTogether.entity.*;
import com.example.getTogether.repository.*;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeetingRecommendService {
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final MeetingRecommendRepository meetingRecommendRepository;
    private final MeetingRecommendDateRepository meetingRecommendDateRepository;
    private final MeetingRecommendFinalDateRepository meetingRecommendFinalDateRepository;
    private final MeetingRecommendPlaceRepository meetingRecommendPlaceRepository;
    private final MeetingRecommendTimeRepository meetingRecommendTimeRepository;
    private final MeetingService meetingService;
    private final ChatgptService chatgptService;

    public Boolean emptyRecommendDate(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        return meetingRecommendDate.isEmpty();
    }

    public void deleteRecommendDate(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get();

        if (!meetingRecommendDate.isEmpty()){
            meetingRecommendDateRepository.deleteAll(meetingRecommendDate);
            meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()-1);
            meetingRecommendRepository.save(meetingRecommend);
        }
    }

    public List<DateTimeDto> getRecommendDate(Long meetingId){
        List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(meetingId).get().getId());

        List<DateTimeDto> localDateTimes = meetingRecommendDate.stream().map(meetingRecommendDate1 -> DateTimeDto.builder().date(meetingRecommendDate1.getMeetingDate()).build()).toList();

        return localDateTimes;
    }

    public void addRecommendDate(RecommendDateDto recommendDateDto){
        List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId(), recommendDateDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();
        
        MeetingRecommendDate date = MeetingRecommendDate.builder()
                .meetingDate(recommendDateDto.getDate())
                .meetingRecommend(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get())
                .member(memberRepository.findById(recommendDateDto.getMemberId()).get())
                .build();

        meetingRecommendDateRepository.save(date);
    }

    public Boolean checkRecommendDate(LeaveMeetingDto recommendDateDto){
        Boolean check = false;
        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();
        meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()+1);
        meetingRecommendRepository.save(meetingRecommend);
        System.out.println("count : "+ meetingService.countMember(recommendDateDto.getMeetingId()));
        System.out.println("checkMember : "+ meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember());

        if(meetingService.countMember(recommendDateDto.getMeetingId())==meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember()){
            List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId());

            Meeting meeting = meetingRepository.findById(recommendDateDto.getMeetingId()).get();

            LocalDateTime now = LocalDate.now().atStartOfDay();

            List<LocalDateTime> localDateTimes = meetingRecommendDate.stream().map(MeetingRecommendDate::getMeetingDate).toList();

            int z = 0;
            do {
                now = now.plusDays(1);
                if (!localDateTimes.contains(now)) {
                    MeetingRecommendFinalDate meetingRecommendFinalDate = MeetingRecommendFinalDate.builder()
                            .meetingRecommend(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get())
                            .meetingDate(now)
                            .build();

                    meetingRecommendFinalDateRepository.save(meetingRecommendFinalDate);
                    z++;
                }
            } while (z != 3);

            meetingRecommend.updateIsDate(false);
            meetingRecommend.updateCheckMember(0);
            meetingService.checkMeetingType(meeting);
            check = true;
        }
        return check;
    }

    public void checkRecommendDate2(RecommendDateDto recommendDateDto){

        if(meetingService.countMember(recommendDateDto.getMeetingId())==meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember()){
            List<MeetingRecommendDate> meetingRecommendDate = meetingRecommendDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId());
            MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();

            List<LocalDateTime> localDateTimes = meetingRecommendDate.stream().map(MeetingRecommendDate::getMeetingDate).toList();

            Map<LocalDateTime, Integer> frequencyMap = new HashMap<>();

            for (LocalDateTime obj : localDateTimes) {
                frequencyMap.put(obj, frequencyMap.getOrDefault(obj, 0) + 1);
            }

            List<Map.Entry<LocalDateTime, Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
            sortedList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            int count = 0;
            for (Map.Entry<LocalDateTime, Integer> entry : sortedList) {
                if (count < 3) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                    // 상위 3개 객체는 여기서 작업할 수 있습니다.

                    MeetingRecommendFinalDate meetingRecommendFinalDate = MeetingRecommendFinalDate.builder()
                            .meetingRecommend(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get())
                            .meetingDate(entry.getKey())
                            .build();

                    meetingRecommendFinalDateRepository.save(meetingRecommendFinalDate);
                    count++;
                } else {
                    break;
                }
            }
            meetingRecommend.updateIsDate(false);
            meetingRecommend.updateCheckMember(0);
        }
    }

    /////////////////////////meetingTime////////////////////////////

    public Boolean emptyRecommendTime(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendTime> meetingRecommendTime = meetingRecommendTimeRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        return meetingRecommendTime.isEmpty();
    }

    public void deleteRecommendTime(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendTime> meetingRecommendTime = meetingRecommendTimeRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get();

        if (!meetingRecommendTime.isEmpty()){
            meetingRecommendTimeRepository.deleteAll(meetingRecommendTime);
            meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()-1);
            meetingRecommendRepository.save(meetingRecommend);
        }
    }

    public List<DateTimeDto> getRecommendFinalDate(Long meetingId){
        List<MeetingRecommendFinalDate> meetingRecommendFinalDate = meetingRecommendFinalDateRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(meetingId).get().getId());

        List<DateTimeDto> localDateTimes = meetingRecommendFinalDate.stream().map(meetingRecommendDate1 -> DateTimeDto.builder().date(meetingRecommendDate1.getMeetingDate()).build()).toList();

        return localDateTimes;
    }

    public List<DateTimeDto> getRecommendTime(Long meetingId){
        List<MeetingRecommendTime> meetingRecommendTime = meetingRecommendTimeRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(meetingId).get().getId());

        List<DateTimeDto> localDateTimes = meetingRecommendTime.stream().map(meetingRecommendDate1 -> DateTimeDto.builder().date(meetingRecommendDate1.getMeetingTime()).build()).toList();

        return localDateTimes;
    }

    public void addRecommendTime(RecommendDateDto recommendDateDto){
        List<MeetingRecommendTime> meetingRecommendTime = meetingRecommendTimeRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId(), recommendDateDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();

        MeetingRecommendTime time = MeetingRecommendTime.builder()
                .meetingTime(recommendDateDto.getDate())
                .meetingRecommend(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get())
                .member(memberRepository.findById(recommendDateDto.getMemberId()).get())
                .build();

        meetingRecommendTimeRepository.save(time);
    }

    public Boolean checkRecommendTime(LeaveMeetingDto recommendDateDto){
        Boolean check = false;
        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();
        meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()+1);
        meetingRecommendRepository.save(meetingRecommend);
        System.out.println("count : "+ meetingService.countMember(recommendDateDto.getMeetingId()));
        System.out.println("checkMember : "+ meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember());


        if(meetingService.countMember(recommendDateDto.getMeetingId())==meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember()){
            List<MeetingRecommendTime> meetingRecommendTime = meetingRecommendTimeRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId());

            Meeting meeting = meetingRepository.findById(recommendDateDto.getMeetingId()).get();

            List<LocalDateTime> localDateTimes = meetingRecommendTime.stream().map(MeetingRecommendTime::getMeetingTime).toList();

            Map<LocalDateTime, Integer> frequencyMap = new HashMap<>();

            for (LocalDateTime obj : localDateTimes) {
                frequencyMap.put(obj, frequencyMap.getOrDefault(obj, 0) + 1);
            }

            List<Map.Entry<LocalDateTime, Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
            sortedList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            int count = 0;
            for (Map.Entry<LocalDateTime, Integer> entry : sortedList) {
                if (count < 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                    // 상위 3개 객체는 여기서 작업할 수 있습니다.

                    meeting.updateStart(entry.getKey());
                    meeting.updateEnd(entry.getKey().plusHours(1));
                    meetingRepository.save(meeting);

                    count++;
                } else {
                    break;
                }
            }
            meetingRecommend.updateIsTime(false);
            meetingRecommend.updateCheckMember(0);
            meetingService.checkMeetingType(meeting);
            check = true;
        }
        return check;
    }


    ///////////////meetingPlace///////////////

    public Boolean emptyRecommendPlace(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendPlace> meetingRecommendPlace = meetingRecommendPlaceRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        return meetingRecommendPlace.isEmpty();
    }

    public void deleteRecommendPlace(LeaveMeetingDto leaveMeetingDto){ //
        List<MeetingRecommendPlace> meetingRecommendPlace = meetingRecommendPlaceRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get().getId(), leaveMeetingDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(leaveMeetingDto.getMeetingId()).get();

        if (!meetingRecommendPlace.isEmpty()){
            meetingRecommendPlaceRepository.deleteAll(meetingRecommendPlace);
            meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()-1);
            meetingRecommendRepository.save(meetingRecommend);
        }
    }

    public void addRecommendPlace(RecommendPlaceDto recommendPlaceDto){
        List<MeetingRecommendPlace> meetingRecommendPlace = meetingRecommendPlaceRepository.findByMeetingRecommendIdAndMemberId(meetingRecommendRepository.findByMeetingId(recommendPlaceDto.getMeetingId()).get().getId(), recommendPlaceDto.getMemberId());

        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendPlaceDto.getMeetingId()).get();

        MeetingRecommendPlace place = MeetingRecommendPlace.builder()
                .latitude(recommendPlaceDto.getLatitude())
                .longitude(recommendPlaceDto.getLongitude())
                .meetingRecommend(meetingRecommendRepository.findByMeetingId(recommendPlaceDto.getMeetingId()).get())
                .member(memberRepository.findById(recommendPlaceDto.getMemberId()).get())
                .build();

        meetingRecommendPlaceRepository.save(place);
    }

    public Boolean checkRecommendPlace(LeaveMeetingDto recommendDateDto){
        Boolean check = false;
        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get();
        meetingRecommend.updateCheckMember(meetingRecommend.getCheckMember()+1);
        meetingRecommendRepository.save(meetingRecommend);
        System.out.println("count : "+ meetingService.countMember(recommendDateDto.getMeetingId()));
        System.out.println("checkMember : "+ meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember());


        if(meetingService.countMember(recommendDateDto.getMeetingId())==meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getCheckMember()){
            List<MeetingRecommendPlace> meetingRecommendPlace = meetingRecommendPlaceRepository.findByMeetingRecommendId(meetingRecommendRepository.findByMeetingId(recommendDateDto.getMeetingId()).get().getId());

            Meeting meeting = meetingRepository.findById(recommendDateDto.getMeetingId()).get();

            double avgLat = calculateAverage(meetingRecommendPlace.stream().map(MeetingRecommendPlace::getLatitude).toList());
            double avgLng = calculateAverage(meetingRecommendPlace.stream().map(MeetingRecommendPlace::getLongitude).toList());

            meetingRecommend.updateFinalLat(avgLat);
            meetingRecommend.updateFinalLng(avgLng);

            meetingRecommend.updateIsPlace(false);
            meetingRecommend.updateCheckMember(0);
            meetingService.checkMeetingType(meeting);
            check = true;
        }
        return check;
    }

    public static double calculateAverage(List<Double> list) {
        if (list == null || list.isEmpty()) {
            return 0.0;
        }

        // 리스트 안의 값들을 더함
        double sum = 0;
        for (double number : list) {
            sum += number;
        }

        // 평균을 계산하여 반환
        return (double) sum / list.size();
    }



    //////////////////finalPlace////////////////

    public PlaceDto getRecommendFinalPlace(Long meetingId){
        Optional<MeetingRecommend> meetingRecommend = meetingRecommendRepository.findByMeetingId(meetingId);

        Meeting meeting  = meetingRepository.findById(meetingId).get();

        PlaceDto placeDto = PlaceDto.builder()
                .latitude(meetingRecommend.get().getFinalLat())
                .longitude(meetingRecommend.get().getFinalLng())
                .createdBy(meeting.getCreatedBy())
                .build();

        return placeDto;
    }

    public void saveRecommendFinalPlace(FinalPlaceDto finalPlaceDto){
        Meeting meeting = meetingRepository.findById(finalPlaceDto.getMeetingId()).get();
        MeetingRecommend meetingRecommend = meetingRecommendRepository.findByMeetingId(finalPlaceDto.getMeetingId()).get();
        meetingRecommend.updateIsFinal(false);

        meeting.updatePlace(finalPlaceDto.getPlace());
        meetingRepository.save(meeting);
        meetingService.checkMeetingType(meeting);
    }

    public String gptRecommendPlace(String place){
        return chatgptService.sendMessage(place+" 주변에서 만나기 좋은 장소 추천해줘");
    }


}
