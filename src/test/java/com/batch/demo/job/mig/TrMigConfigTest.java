package com.batch.demo.job.mig;

import com.batch.demo.core.domain.account.AccountRepository;
import com.batch.demo.core.domain.orders.OrderRepository;
import com.batch.demo.core.domain.orders.Orders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {com.batch.demo.SpringBatchTestConfig.class, TrMigConfig.class})
class TrMigConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Job에 데이터가 없을 경우 성공")
    void success_noData() throws Exception {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(0).isEqualTo(accountRepository.count());
    }

    @Test
    @DisplayName("데이터 생성 후 마이그레이션이 성공했는지 확인")
    void success_existData() throws Exception {
        // given : 데이터 생성
        Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null, "naver gift", 15000, new Date());

        orderRepository.save(orders1);
        orderRepository.save(orders2);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(2).isEqualTo(accountRepository.count());
    }
}