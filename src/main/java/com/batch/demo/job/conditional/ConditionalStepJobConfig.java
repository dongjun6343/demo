package com.batch.demo.job.conditional;


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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    /**
     * desc: 상태값에 따른 분기처리
     * run param: --job.name=ConditionalStepJob
     */
    @Bean
    public Job ConditionalStepJob(JobRepository jobRepository, Step conditionalStartStep, Step conditionalAllStep, Step conditionalFailStep, Step conditionalCompletedStep) {
        return new JobBuilder("ConditionalStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep).on("FAILED").to(conditionalFailStep) // 실패 시
                .from(conditionalStartStep).on("COMPLETED").to(conditionalCompletedStep) // 성공 시
                .from(conditionalStartStep).on("*").to(conditionalAllStep) // 예상했던 실패나 성공이 아닌 값이 나올 경우
                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalStartStep(JobRepository jobRepository, Tasklet conditionalStartStepTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("conditionalStartStep", jobRepository)
                .tasklet(conditionalStartStepTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalFailStep(JobRepository jobRepository, Tasklet conditionalFailStepTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("conditionalFailStep", jobRepository)
                .tasklet(conditionalFailStepTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalCompletedStep(JobRepository jobRepository, Tasklet conditionalCompletedStepTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("conditionalCompletedStep", jobRepository)
                .tasklet(conditionalCompletedStepTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalAllStep(JobRepository jobRepository, Tasklet conditionalAllStepTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("conditionalAllStep", jobRepository)
                .tasklet(conditionalAllStepTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet conditionalStartStepTasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("conditional Start Step !!!");
            return RepeatStatus.FINISHED; // 정상적으로 처리했으므로 Completed Step 실행
            //throw new Exception("ERROR!!!"); // 비정상적인 에러 발생 -> Fail Step 실행
        });
    }

    @Bean
    @StepScope
    public Tasklet conditionalFailStepTasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("conditional Fail Step !!!");
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    @StepScope
    public Tasklet conditionalCompletedStepTasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("conditional Completed Step !!!");
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    @StepScope
    public Tasklet conditionalAllStepTasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("conditional All Step !!!");
            return RepeatStatus.FINISHED;
        });
    }
}
