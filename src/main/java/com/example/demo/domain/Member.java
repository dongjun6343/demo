package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Entity : 테이블과 링크될 클래스
 *          EX) SaleManager.java -> sales_manager table
 */

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50 , nullable = false)
    private String username;

    @Builder
    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
