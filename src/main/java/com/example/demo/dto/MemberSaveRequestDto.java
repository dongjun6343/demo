package com.example.demo.dto;

import com.example.demo.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {
    private String name;
    private String phone;
    private String address;

    @Builder
    public MemberSaveRequestDto(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // username(name) 추가 안하면  NULL not allowed for column "USERNAME" 에러발생.
    public Member toEntity(){
        return Member.builder().
                username(name).
                phone(phone).
                address(address).
                build();
    }
}
