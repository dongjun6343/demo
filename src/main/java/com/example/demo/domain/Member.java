package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 50, nullable = false)
    private String phone;

    @Column(length = 200, nullable = false)
    private String address;

    @Builder
    public Member(Long id, String username, String phone, String address) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.address = address;
    }

    public void update(String username ,String phone, String address){
        this.username = username;
        this.phone = phone;
        this.address = address;
    }
}
