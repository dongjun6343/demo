package com.example.demo.dto;

import com.example.demo.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {
    private String name;

    @Builder
    public MemberSaveRequestDto(String name) {
        this.name = name;
    }

    // username(name) 추가 안하면  NULL not allowed for column "USERNAME" 에러발생.
    public Member toEntity(){
        return Member.builder().username(name).build();
    }
}
