package com.ssafy.bablog.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class BatchJobScheduler {

    private static final Logger log = LoggerFactory.getLogger(BatchJobScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job dailyMealInitJob;
    private final Job dailyReportJob;
    private final Job weeklyReportJob;

    public BatchJobScheduler(JobLauncher jobLauncher,
                             Job dailyMealInitJob,
                             Job dailyReportJob,
                             Job weeklyReportJob) {
        this.jobLauncher = jobLauncher;
        this.dailyMealInitJob = dailyMealInitJob;
        this.dailyReportJob = dailyReportJob;
        this.weeklyReportJob = weeklyReportJob;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyMealInitJob() {
        LocalDate targetDate = LocalDate.now();
        runJob(dailyMealInitJob, new JobParametersBuilder()
                .addString("targetDate", targetDate.toString())
                .toJobParameters());
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void runDailyReportJob() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        runJob(dailyReportJob, new JobParametersBuilder()
                .addString("targetDate", targetDate.toString())
                .toJobParameters());
    }

    @Scheduled(cron = "0 15 0 * * *")
    public void runWeeklyReportJob() {
        LocalDate weekStart = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        runJob(weeklyReportJob, new JobParametersBuilder()
                .addString("weekStart", weekStart.toString())
                .addString("weekEnd", weekEnd.toString())
                .toJobParameters());
    }

    private void runJob(Job job, org.springframework.batch.core.JobParameters parameters) {
        try {
            jobLauncher.run(job, parameters);
        } catch (Exception ex) {
            log.error("Batch job failed: jobName={}, message={}", job.getName(), ex.getMessage(), ex);
        }
    }
}
