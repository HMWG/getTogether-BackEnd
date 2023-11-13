package com.example.getTogether.service;

import com.example.getTogether.dto.MemberJoinDto;
import com.example.getTogether.entity.Member;
import com.example.getTogether.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void join(Member member){
        if (!memberRepository.existsById(member.getId())) {
            memberRepository.save(member);
            System.out.println(member.getUsername()+"님이 회원가입 하였습니다. ID : "+member.getId());
        }
        else {
            System.out.println(member.getUsername()+ "님은 이미 가입하였습니다. ID : " + member.getId());
        }
    }
}
