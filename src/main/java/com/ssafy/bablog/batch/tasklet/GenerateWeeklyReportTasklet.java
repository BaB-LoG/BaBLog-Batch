package com.ssafy.bablog.batch.tasklet;

import com.ssafy.bablog.batch.failure.BatchFailureLogService;
import com.ssafy.bablog.batch.member.MemberIdProvider;
import com.ssafy.bablog.batch.service.ReportBatchService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class GenerateWeeklyReportTasklet implements Tasklet {

    private final MemberIdProvider memberIdProvider;
    private final ReportBatchService reportBatchService;
    private final BatchFailureLogService failureLogService;

    public GenerateWeeklyReportTasklet(MemberIdProvider memberIdProvider,
                                       ReportBatchService reportBatchService,
                                       BatchFailureLogService failureLogService) {
        this.memberIdProvider = memberIdProvider;
        this.reportBatchService = reportBatchService;
        this.failureLogService = failureLogService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
        LocalDate weekStart = LocalDate.parse((String) params.get("weekStart"));
        LocalDate weekEnd = LocalDate.parse((String) params.get("weekEnd"));
        String jobName = chunkContext.getStepContext().getJobName();
        String stepName = chunkContext.getStepContext().getStepName();

        @SuppressWarnings("unchecked")
        List<Long> memberIds = (List<Long>) chunkContext.getStepContext()
                .getStepExecutionContext()
                .get("memberIds");
        if (memberIds == null || memberIds.isEmpty()) {
            memberIds = memberIdProvider.fetchAllMemberIds();
        }
        for (Long memberId : memberIds) {
            try {
                reportBatchService.generateWeeklyReport(memberId, weekStart, weekEnd);
            } catch (Exception ex) {
                failureLogService.logFailure(jobName, stepName, memberId, null, weekStart, weekEnd, ex);
            }
        }

        return RepeatStatus.FINISHED;
    }
}
