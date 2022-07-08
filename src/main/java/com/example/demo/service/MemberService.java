package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.dto.MemberSaveRequestDto;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberSaveRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }


    @Transactional
    public Long update(Long id, MemberSaveRequestDto requestDto) {

        // address, phone
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 회원번호가 없습니다. id =" + id));
        member.update(requestDto.getName(),requestDto.getPhone(), requestDto.getAddress());
        return id;
    }

    public MemberResponseDto findById(Long id) {
        Member entity = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 회원번호가 없습니다. id = " + id
        ));
        return new MemberResponseDto(entity);
    }
}
