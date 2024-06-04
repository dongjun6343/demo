package com.batch.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케쥴링을 하기 위해서 추가
@SpringBootApplication
//@EnableBatchProcessing 2.x 에서는 사용하지만, 3.x 부터는 Spring Batch 기본설정이 되므로 삭제
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
