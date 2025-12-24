package com.ssafy.bablog.batch.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("test")
public class BatchStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(BatchStartupRunner.class);
    private static final LocalDate BASE_DATE = LocalDate.of(2025, 12, 22);

    private final JobLauncher jobLauncher;
    private final Job dailyMealInitJob;
    private final Job dailyReportJob;
    private final Job weeklyReportJob;

    public BatchStartupRunner(JobLauncher jobLauncher,
                              Job dailyMealInitJob,
                              Job dailyReportJob,
                              Job weeklyReportJob) {
        this.jobLauncher = jobLauncher;
        this.dailyMealInitJob = dailyMealInitJob;
        this.dailyReportJob = dailyReportJob;
        this.weeklyReportJob = weeklyReportJob;
    }

    @Override
    public void run(ApplicationArguments args) {
        LocalDate dailyMealDate = BASE_DATE;
        LocalDate dailyReportDate = BASE_DATE.minusDays(1);
        LocalDate weekStart = LocalDate.of(2025, 12, 15);
        LocalDate weekEnd = LocalDate.of(2025, 12, 21);

        runJob(dailyMealInitJob, new JobParametersBuilder()
                .addString("targetDate", dailyMealDate.toString())
                .toJobParameters());
        runJob(dailyReportJob, new JobParametersBuilder()
                .addString("targetDate", dailyReportDate.toString())
                .toJobParameters());
        runJob(weeklyReportJob, new JobParametersBuilder()
                .addString("weekStart", weekStart.toString())
                .addString("weekEnd", weekEnd.toString())
                .toJobParameters());
    }

    private void runJob(Job job, JobParameters parameters) {
        JobParameters withRunId = new JobParametersBuilder(parameters)
                .addLong("runAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, withRunId);
        } catch (Exception ex) {
            log.error("Startup batch job failed: jobName={}, message={}", job.getName(), ex.getMessage(), ex);
        }
    }
}
