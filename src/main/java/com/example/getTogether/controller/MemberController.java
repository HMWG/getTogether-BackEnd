package com.example.getTogether.controller;

import com.example.getTogether.entity.Member;
import com.example.getTogether.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/join", produces = "application/json")
    public ResponseEntity<Member> joinMember(@RequestBody Member member){

        memberService.join(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
