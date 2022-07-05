package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        //중복 회원 검증 로직 필요.
        memberRepository.save(member);
        return member.getId();
    }
}
