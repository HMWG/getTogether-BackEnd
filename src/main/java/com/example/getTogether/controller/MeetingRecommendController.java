package com.example.getTogether.controller;

import com.example.getTogether.dto.*;
import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.MeetingRecommendDate;
import com.example.getTogether.entity.MeetingRecommendPlace;
import com.example.getTogether.service.MeetingRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingRecommendController {
    private final MeetingRecommendService meetingRecommendService;


    @PostMapping("/recommend/date/empty")//내 아이디로 투표를 했는지 확인하는(투표한 date가 있는지 확인)
    public ResponseEntity<Boolean> emptyDate(@RequestBody LeaveMeetingDto leaveMeetingDto){

        Boolean empty = meetingRecommendService.emptyRecommendDate(leaveMeetingDto);

        return new ResponseEntity<>(empty, HttpStatus.OK);
    }

    @DeleteMapping("/recommend/date/delete")//투표를 했다면 투표 내역을 삭제하는
    public ResponseEntity<MeetingRecommendDate> deleteDate(@RequestBody LeaveMeetingDto leaveMeetingDto){

        meetingRecommendService.deleteRecommendDate(leaveMeetingDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/date/add")//투표하는
    public ResponseEntity<MeetingRecommendDate> addDate(@RequestBody RecommendDateDto recommendDateDto){

        meetingRecommendService.addRecommendDate(recommendDateDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/date")//해당 미팅에 투표한 날짜를 받아오는
    public ResponseEntity<List<DateTimeDto>> getDate(@RequestBody LeaveMeetingDto leaveMeetingDto){

        List<DateTimeDto> list = meetingRecommendService.getRecommendDate(leaveMeetingDto.getMeetingId());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/recommend/date/check")//모두 투표했다면 투표 날짜중 상위 3개를 뽑아 finalDate를 만드는
    public ResponseEntity<Boolean> checkDate(@RequestBody LeaveMeetingDto leaveMeetingDto){
        Boolean check = false;

        check = meetingRecommendService.checkRecommendDate(leaveMeetingDto);

        return new ResponseEntity<>(check,HttpStatus.OK);
    }

    ////////////////meetingTime////////////////

    @PostMapping("/recommend/time")//해당 미팅에 투표한 날짜를 받아오는
    public ResponseEntity<List<DateTimeDto>> getFinalTime(@RequestBody LeaveMeetingDto leaveMeetingDto){

        List<DateTimeDto> list = meetingRecommendService.getRecommendFinalDate(leaveMeetingDto.getMeetingId());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/recommend/time/empty")//내 아이디로 투표를 했는지 확인하는(투표한 date가 있는지 확인)
    public ResponseEntity<Boolean> emptyTime(@RequestBody LeaveMeetingDto leaveMeetingDto){

        Boolean empty = meetingRecommendService.emptyRecommendTime(leaveMeetingDto);

        return new ResponseEntity<>(empty, HttpStatus.OK);
    }

    @DeleteMapping("/recommend/time/delete")//투표를 했다면 투표 내역을 삭제하는
    public ResponseEntity<MeetingRecommendDate> deleteTime(@RequestBody LeaveMeetingDto leaveMeetingDto){

        meetingRecommendService.deleteRecommendTime(leaveMeetingDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/time/add")//투표하는
    public ResponseEntity<MeetingRecommendDate> addTime(@RequestBody RecommendDateDto recommendDateDto){

        meetingRecommendService.addRecommendTime(recommendDateDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/time/check")//모두 투표했다면 투표 날짜중 상위 3개를 뽑아 finalDate를 만드는
    public ResponseEntity<Boolean> checkTime(@RequestBody LeaveMeetingDto leaveMeetingDto){
        Boolean check = false;

        check = meetingRecommendService.checkRecommendTime(leaveMeetingDto);

        return new ResponseEntity<>(check,HttpStatus.OK);
    }

    //////////////////Place/////////////////

    @PostMapping("/recommend/place/empty")//내 아이디로 투표를 했는지 확인하는(투표한 date가 있는지 확인)
    public ResponseEntity<Boolean> emptyPlace(@RequestBody LeaveMeetingDto leaveMeetingDto){

        Boolean empty = meetingRecommendService.emptyRecommendPlace(leaveMeetingDto);

        return new ResponseEntity<>(empty, HttpStatus.OK);
    }

    @DeleteMapping("/recommend/place/delete")//투표를 했다면 투표 내역을 삭제하는
    public ResponseEntity<MeetingRecommendPlace> deletePlace(@RequestBody LeaveMeetingDto leaveMeetingDto){

        meetingRecommendService.deleteRecommendPlace(leaveMeetingDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/place/add")//투표하는
    public ResponseEntity<MeetingRecommendPlace> addPlace(@RequestBody RecommendPlaceDto recommendPlaceDto){

        meetingRecommendService.addRecommendPlace(recommendPlaceDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/place/check")//모두 투표했다면 투표 날짜중 상위 3개를 뽑아 finalDate를 만드는
    public ResponseEntity<Boolean> checkPlace(@RequestBody LeaveMeetingDto leaveMeetingDto){
        Boolean check = false;

        check = meetingRecommendService.checkRecommendPlace(leaveMeetingDto);

        return new ResponseEntity<>(check,HttpStatus.OK);
    }


    //////////finalPlace////////////

    @PostMapping("/recommend/final")//해당 미팅에 투표한 날짜를 받아오는
    public ResponseEntity<PlaceDto> getFinalPlace(@RequestBody LeaveMeetingDto leaveMeetingDto){

        PlaceDto placeDto = meetingRecommendService.getRecommendFinalPlace(leaveMeetingDto.getMeetingId());

        return new ResponseEntity<>(placeDto, HttpStatus.OK);
    }

    @PostMapping("/recommend/final/save")//해당 미팅에 투표한 날짜를 받아오는
    public ResponseEntity<PlaceDto> saveFinalPlace(@RequestBody FinalPlaceDto finalPlaceDto){

        meetingRecommendService.saveRecommendFinalPlace(finalPlaceDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/recommend/final/gpt")//해당 미팅에 투표한 날짜를 받아오는
    public ResponseEntity<String> gptFinalPlace(@RequestBody FinalPlaceDto finalPlaceDto){

        //String recommend = meetingRecommendService.gptRecommendPlace(address);
        String recommend = "희망하는 장소를 골라주세요";
        System.out.println("확인");

        return new ResponseEntity<>(recommend, HttpStatus.OK);
    }
}
