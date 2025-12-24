package com.ssafy.bablog.batch.service;

import java.time.LocalDate;

public interface ReportBatchService {
    void generateDailyReport(Long memberId, LocalDate targetDate);

    void generateWeeklyReport(Long memberId, LocalDate weekStart, LocalDate weekEnd);
}
