package com.ssafy.bablog.batch.failure;

import java.time.LocalDate;

public interface BatchFailureLogService {
    void logFailure(String jobName,
                    String stepName,
                    Long memberId,
                    LocalDate targetDate,
                    LocalDate weekStart,
                    LocalDate weekEnd,
                    Throwable throwable);
}
