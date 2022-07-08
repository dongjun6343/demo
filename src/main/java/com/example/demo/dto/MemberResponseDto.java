package com.example.demo.dto;

import com.example.demo.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MemberResponseDto : Entity의 필드 중 일부만 사용하므로 생성자로 Entity를 받아 필드에 값을 넣는다.
 * 굳이 모든 필드를 가진 생성자가 필요하지 않으므로 Dto는 entity만 받아서 처리한다.
 */
@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;

    public MemberResponseDto(Member entity) {
        this.id = entity.getId();
        this.name = entity.getUsername();
    }
}
