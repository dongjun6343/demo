package com.batch.demo.job.validParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ValidateJobConfig {

    @Bean
    public Job validateParamJob(JobRepository jobRepository, Step validateParamStep) {
        return new JobBuilder("validateParamJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(validateParamStep)
                .build();
    }


    @Bean
    public Step validateParamStep(JobRepository jobRepository, Tasklet validateTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("validateParamStep", jobRepository)
                .tasklet(validateTasklet, platformTransactionManager) // tx 처리
                .build();
    }

    //  -fileName=test.csv -> @Value를 통해서 fileName을 가져옴. (테스트 필요)
    // JobParameters는 Scope Bean을 생성할때만 사용 가능하다. 즉, @StepScope, @JobScope Bean을 생성할때만 JobParameters가 생성된다.
    //    @Bean
//    public Tasklet validateTasklet(@Value("#{jobParameter[fileName]}") String fileName) {
//        return ((contribution, chunkContext) -> {
//            log.info("fileName : {} ", fileName);
//            log.info(">>>>>>>>> validateParam Tasklet !! ");
//            return RepeatStatus.FINISHED;
//        });
//    }
    @Bean
    public Tasklet validateTasklet() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>>>>>> validateParam Tasklet !! ");
            return RepeatStatus.FINISHED;
        });
    }
}
