package com.ssafy.bablog.batch.failure;

import com.ssafy.bablog.batch.failure.mapper.BatchFailureLogMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MapperBatchFailureLogService implements BatchFailureLogService {

    private static final int MAX_MESSAGE_LENGTH = 1000;

    private final BatchFailureLogMapper mapper;

    public MapperBatchFailureLogService(BatchFailureLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void logFailure(String jobName,
                           String stepName,
                           Long memberId,
                           LocalDate targetDate,
                           LocalDate weekStart,
                           LocalDate weekEnd,
                           Throwable throwable) {
        String message = throwable.getMessage();
        if (message != null && message.length() > MAX_MESSAGE_LENGTH) {
            message = message.substring(0, MAX_MESSAGE_LENGTH);
        }
        mapper.insertFailure(
                jobName,
                stepName,
                memberId,
                targetDate,
                weekStart,
                weekEnd,
                throwable.getClass().getName(),
                message
        );
    }
}
