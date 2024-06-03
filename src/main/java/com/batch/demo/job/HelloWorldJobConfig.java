package com.batch.demo.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
// @RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
public class HelloWorldJobConfig {

    // private final JobBuilderFactory jobBuilderFactory; // 2.x job 생성하기 위해서 주입

    // private final StepBuilderFactory stepBuilderFactory; // 2.x step 생성하기 위해서 주입

    /**
     * 2.x버전에서는 의존성 주입해서 사용했지만,
     * JobBuilder 객체를 직접 사용하여 아래와 같이 설정하는 것이 권장됨.
     */

    @Bean
    public Job helloWorldJob(JobRepository jobRepository, Step simpleStep1) {
        return new JobBuilder("helloWorldJob", jobRepository)
                .start(simpleStep1)
                .build();
    }


    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet testTasklet() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>>>>>> Hello World Step1");
            return RepeatStatus.FINISHED;
        });
    }
}
