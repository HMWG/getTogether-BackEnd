package com.example.getTogether.controller;

import com.example.getTogether.dto.*;
import com.example.getTogether.entity.Meeting;
import com.example.getTogether.entity.Member;
import com.example.getTogether.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @GetMapping("/meetings")
    public ResponseEntity<Map<String, Object>> findMeetings(@RequestParam Long id){

        Map<String, Object> result = new HashMap<>();

        List<MyMeetingDto> meetings = meetingService.findMeetings(id);
        List<MyMeetingDto> myMeetings = meetingService.findMyMeetings(id);

        result.put("meetings", meetings);
        result.put("myMeetings", myMeetings);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/meeting")
    public ResponseEntity<MyMeetingDto> findMeeting(@RequestParam Long id){

        MyMeetingDto myMeetingDto = meetingService.findMeeting(id);

        return new ResponseEntity<>(myMeetingDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Meeting> createMyMeeting(@RequestBody MeetingCreateDto meetingCreateDto){
        meetingService.createMeeting(meetingCreateDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<String> countMembers(@RequestParam Long id){

        int result = meetingService.countMember(id);

        return new ResponseEntity<>(Integer.toString(result), HttpStatus.OK);
    }

    @DeleteMapping("/meetings/delete")
    public ResponseEntity<Meeting> deleteMeeting(@RequestBody NoticeIdDto noticeIdDto){

        meetingService.deleteMeeting(noticeIdDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/meetings/leave")
    public ResponseEntity<Meeting> leaveMeeting(@RequestBody LeaveMeetingDto leaveMeetingDto){

        meetingService.leaveMeeting(leaveMeetingDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
