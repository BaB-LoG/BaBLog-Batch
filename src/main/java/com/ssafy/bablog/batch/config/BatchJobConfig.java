package com.ssafy.bablog.batch.config;

import com.ssafy.bablog.batch.member.MemberIdProvider;
import com.ssafy.bablog.batch.partition.MemberIdListPartitioner;
import com.ssafy.bablog.batch.tasklet.CreateDailyMealsTasklet;
import com.ssafy.bablog.batch.tasklet.GenerateDailyReportTasklet;
import com.ssafy.bablog.batch.tasklet.GenerateWeeklyReportTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    private static final int GRID_SIZE = 8;

    @Bean
    public Job dailyMealInitJob(JobRepository jobRepository, Step createDailyMealsPartitionStep) {
        return new JobBuilder("dailyMealInitJob", jobRepository)
                .start(createDailyMealsPartitionStep)
                .build();
    }

    @Bean
    public Job dailyReportJob(JobRepository jobRepository, Step generateDailyReportPartitionStep) {
        return new JobBuilder("dailyReportJob", jobRepository)
                .start(generateDailyReportPartitionStep)
                .build();
    }

    @Bean
    public Job weeklyReportJob(JobRepository jobRepository, Step generateWeeklyReportPartitionStep) {
        return new JobBuilder("weeklyReportJob", jobRepository)
                .start(generateWeeklyReportPartitionStep)
                .build();
    }

    // 파티셔닝 기준 메타데이터가 담긴 파티셔너
    @Bean
    public Partitioner memberIdPartitioner(MemberIdProvider memberIdProvider) {
        return new MemberIdListPartitioner(memberIdProvider);
    }

    // 멀티 스레드 실행기 객체
    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("batch-worker-");
        executor.setCorePoolSize(GRID_SIZE);
        executor.setMaxPoolSize(GRID_SIZE);
        executor.setQueueCapacity(0);
        executor.initialize();
        return executor;
    }

    @Bean(name = "transactionManager")
    public ResourcelessTransactionManager resourcelessTransactionManager() {
        return new ResourcelessTransactionManager();
    }

//    @Bean(name = "transactionManager")
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    // ------------------------------------------  WorkerStep  -----------------------------------------------------

    @Bean
    public Step createDailyMealsWorkerStep(JobRepository jobRepository,
                                           ResourcelessTransactionManager transactionManager,
                                           CreateDailyMealsTasklet tasklet) {
        return new StepBuilder("createDailyMealsWorkerStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step generateDailyReportWorkerStep(JobRepository jobRepository,
                                              ResourcelessTransactionManager transactionManager,
                                              GenerateDailyReportTasklet tasklet) {
        return new StepBuilder("generateDailyReportWorkerStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step generateWeeklyReportWorkerStep(JobRepository jobRepository,
                                               ResourcelessTransactionManager transactionManager,
                                               GenerateWeeklyReportTasklet tasklet) {
        return new StepBuilder("generateWeeklyReportWorkerStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    // ------------------------------------------  partitionStep  -----------------------------------------------------

    @Bean
    public Step createDailyMealsPartitionStep(JobRepository jobRepository,
                                              Step createDailyMealsWorkerStep,
                                              Partitioner memberIdPartitioner,
                                              TaskExecutor batchTaskExecutor) {
        return new StepBuilder("createDailyMealsPartitionStep", jobRepository)
                .partitioner(createDailyMealsWorkerStep.getName(), memberIdPartitioner)
                .step(createDailyMealsWorkerStep)
                .taskExecutor(batchTaskExecutor)
                .gridSize(GRID_SIZE)
                .build();
    }

    @Bean
    public Step generateDailyReportPartitionStep(JobRepository jobRepository,
                                                 Step generateDailyReportWorkerStep,
                                                 Partitioner memberIdPartitioner,
                                                 TaskExecutor batchTaskExecutor) {
        return new StepBuilder("generateDailyReportPartitionStep", jobRepository)
                .partitioner(generateDailyReportWorkerStep.getName(), memberIdPartitioner)
                .step(generateDailyReportWorkerStep)
                .taskExecutor(batchTaskExecutor)
                .gridSize(GRID_SIZE)
                .build();
    }

    @Bean
    public Step generateWeeklyReportPartitionStep(JobRepository jobRepository,
                                                  Step generateWeeklyReportWorkerStep,
                                                  Partitioner memberIdPartitioner,
                                                  TaskExecutor batchTaskExecutor) {
        return new StepBuilder("generateWeeklyReportPartitionStep", jobRepository)
                .partitioner(generateWeeklyReportWorkerStep.getName(), memberIdPartitioner)
                .step(generateWeeklyReportWorkerStep)
                .taskExecutor(batchTaskExecutor)
                .gridSize(GRID_SIZE)
                .build();
    }
}
