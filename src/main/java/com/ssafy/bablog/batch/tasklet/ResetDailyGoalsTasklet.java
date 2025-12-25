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
public class ResetDailyGoalsTasklet implements Tasklet {

    private final GoalResetService goalResetService;
    private final BatchFailureLogService failureLogService;

    public ResetDailyGoalsTasklet(GoalResetService goalResetService,
                                  BatchFailureLogService failureLogService) {
        this.goalResetService = goalResetService;
        this.failureLogService = failureLogService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
        String jobName = chunkContext.getStepContext().getJobName();
        String stepName = chunkContext.getStepContext().getStepName();
        LocalDate targetDate = LocalDate.parse((String) params.get("targetDate"));

        try {
            goalResetService.resetDailyGoals();
        } catch (Exception ex) {
            failureLogService.logFailure(jobName, stepName, null, targetDate, null, null, ex);
        }

        return RepeatStatus.FINISHED;
    }
}
