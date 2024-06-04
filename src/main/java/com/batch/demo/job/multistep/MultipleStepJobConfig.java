package com.batch.demo.job.multistep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    /**
     * desc: 다중 step을 사용하기 및 step to step 데이터 전달
     * run param: --job.name=multipleStepJob
     */

    @Bean
    public Job multipleStepJob(JobRepository jobRepository, Step multipleStep1, Step multipleStep2, Step multipleStep3) {
        return new JobBuilder("multipleStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1)
                .next(multipleStep2)
                .next(multipleStep3)
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep1(JobRepository jobRepository, Tasklet multipleStep1Tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("multipleStep1", jobRepository)
                .tasklet(multipleStep1Tasklet, platformTransactionManager)
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep2(JobRepository jobRepository, Tasklet multipleStep2Tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("multipleStep2", jobRepository)
                .tasklet(multipleStep2Tasklet, platformTransactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step multipleStep3(JobRepository jobRepository, Tasklet multipleStep3Tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("multipleStep3", jobRepository)
                .tasklet(multipleStep3Tasklet, platformTransactionManager)
                .build();
    }

    /**
     * step 1
     */
    @StepScope
    @Bean
    public Tasklet multipleStep1Tasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("step1");
            return RepeatStatus.FINISHED;
        });
    }

    /**
     * step2
     * executionContext.put("someKey", "hello!!"); 추가
     */
    @Bean
    @StepScope
    public Tasklet multipleStep2Tasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("step2");

            ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
            executionContext.put("someKey", "hello!!");
            return RepeatStatus.FINISHED;
        });
    }

    /**
     * step3
     * step2에서 추가한 키값에 있는 벨류값 찾기
     */
    @Bean
    @StepScope
    public Tasklet multipleStep3Tasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("step3");
            ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
            System.out.println("executionContext.get() = " + executionContext.get("someKey"));
            return RepeatStatus.FINISHED;
        });
    }
}
