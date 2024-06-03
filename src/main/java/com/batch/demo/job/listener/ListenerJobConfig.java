package com.batch.demo.job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ListenerJobConfig {
    @Bean
    public Job jobListenerJob(JobRepository jobRepository, Step jobListenerStep) {
        return new JobBuilder("jobListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(jobListenerStep)
                .listener(new JobLoggerListener())
                .build();
    }

    @Bean
    public Step jobListenerStep(JobRepository jobRepository, Tasklet listenerTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobListenerStep", jobRepository)
                .tasklet(listenerTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet listenerTasklet() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>>>>>> listenerTasklet start");
            // 원하는 비즈니스 로직 작성
            // return RepeatStatus.FINISHED;
            throw new Exception("FAIL !!!");
        });
    }
}

