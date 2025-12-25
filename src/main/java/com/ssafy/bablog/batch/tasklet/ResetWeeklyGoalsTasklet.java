package com.ssafy.bablog.batch.tasklet;

import com.ssafy.bablog.batch.failure.BatchFailureLogService;
import com.ssafy.bablog.goal.service.GoalResetService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class ResetWeeklyGoalsTasklet implements Tasklet {

    private final GoalResetService goalResetService;
    private final BatchFailureLogService failureLogService;

    public ResetWeeklyGoalsTasklet(GoalResetService goalResetService,
                                   BatchFailureLogService failureLogService) {
        this.goalResetService = goalResetService;
        this.failureLogService = failureLogService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
        String jobName = chunkContext.getStepContext().getJobName();
        String stepName = chunkContext.getStepContext().getStepName();
        LocalDate weekStart = LocalDate.parse((String) params.get("weekStart"));
        LocalDate weekEnd = LocalDate.parse((String) params.get("weekEnd"));

        try {
            goalResetService.resetWeeklyGoals();
        } catch (Exception ex) {
            failureLogService.logFailure(jobName, stepName, null, null, weekStart, weekEnd, ex);
        }

        return RepeatStatus.FINISHED;
    }
}
