package com.example.playground.gift.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    private final JobLauncher jobLauncher;

    private final Job giftJob;

    public JobController(JobLauncher jobLauncher, Job giftJob) {
        this.jobLauncher = jobLauncher;
        this.giftJob = giftJob;
    }

    @GetMapping("/gift")
    @Scheduled(cron = "${app.job.gift.cron}", zone = "${app.zone-id}")
    public void startGiftJob() throws Exception {
        startJob();
    }

    private void startJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("launch_date", new Date())
                .toJobParameters();
        jobLauncher.run(giftJob, jobParameters);
    }
}
