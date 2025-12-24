package com.ssafy.bablog.batch.service;

import com.ssafy.bablog.report.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CoreReportBatchService implements ReportBatchService {

    private final ReportService reportService;

    public CoreReportBatchService(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void generateDailyReport(Long memberId, LocalDate targetDate) {
        reportService.generateDailyReport(memberId, targetDate);
    }

    @Override
    public void generateWeeklyReport(Long memberId, LocalDate weekStart, LocalDate weekEnd) {
        reportService.generateWeeklyReport(memberId, weekStart, weekEnd);
    }
}
