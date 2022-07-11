package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *  @SpringBootApplication : 프로젝트 최상단에 위치
 *  						1. 스프링부트의 자동설정 및 스프링빈 읽기
 *
 *  SpringApplication.run : 1. 내장WAS사용. (스프링부트에서는 내장WAS 권장)
 *  						2. 외부에 WAS를 두지 않고 애플리케이션을 실행할 때 내부 WAS 실행.
 *  					 	3. 언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있으므로
 */

// @EnableJpaAuditing : Spring Audit 기능을 활용하기 위해서 필요한 어노테이션.
@EnableJpaAuditing
@SpringBootApplication
public class DemoPdjApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPdjApplication.class, args);
	}

}
