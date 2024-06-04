package com.batch.demo.job.conditional;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    @Bean
    public Job helloWorldJob(JobRepository jobRepository, Step simpleStep1) {
        return new JobBuilder("helloWorldJob", jobRepository)
                .start(simpleStep1)
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet testTasklet() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>>>>>> Hello World Step1");
            return RepeatStatus.FINISHED;
        });
    }
}
