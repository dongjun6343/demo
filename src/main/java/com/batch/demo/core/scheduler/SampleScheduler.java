package com.batch.demo.core.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SampleScheduler {

    /**
     * 스케쥴링을 활용하여 JOB을 실행할때는 JobLauncher를 설정한다.
     */

    private final JobLauncher jobLauncher;
    private final Job helloWorldJob;

    // 1분씩 helloWorldJob을 실행시킨다.
    @Scheduled(cron = "0 */1 * * * *")
    public void helloWorldJobRun()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis(), Long.class)));
        jobLauncher.run(helloWorldJob, jobParameters);
    }


}
